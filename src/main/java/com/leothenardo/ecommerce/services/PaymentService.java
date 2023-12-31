package com.leothenardo.ecommerce.services;


import com.leothenardo.ecommerce.dtos.PaymentPixDetailsResponseDTO;
import com.leothenardo.ecommerce.dtos.UserDTO;
import com.leothenardo.ecommerce.dtos.OrderDTO;
import com.leothenardo.ecommerce.dtos.PaymentUrlRequestDTO;
import com.leothenardo.ecommerce.dtos.sensitive.ProcessCheckoutSecureDTO;
import com.leothenardo.ecommerce.dtos.sensitive.TokenizeCardRequestSecureDTO;
import com.leothenardo.ecommerce.dtos.mail.ConfirmationOrderMailInput;
import com.leothenardo.ecommerce.dtos.webhooks.AsaasPayWebhook;
import com.leothenardo.ecommerce.gateways.EmailProvider;
import com.leothenardo.ecommerce.gateways.PaymentGatewayProvider;
import com.leothenardo.ecommerce.gateways.models.asaas.GetPaymentPixQRCodeResponse;
import com.leothenardo.ecommerce.models.*;
import com.leothenardo.ecommerce.repositories.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class PaymentService {
	private static final Logger log = Logger.getLogger(PaymentService.class.getName());
	private final UserService userService;
	private final OrderService orderService;
	private final PaymentGatewayProvider paymentGateway;
	private final OrderRepository orderRepository;
	private final EmailProvider emailProvider;
	private final NotificationService notificationService;
	private final ProductService productService;
	private final SSEService sseService;
	private final AuthService authService;

	public PaymentService(UserService userService, OrderService orderService, PaymentGatewayProvider paymentGateway, OrderRepository orderRepository, EmailProvider emailProvider, NotificationService notificationService, ProductService productService, SSEService sseService, AuthService authService) {
		this.userService = userService;
		this.orderService = orderService;
		this.paymentGateway = paymentGateway;
		this.orderRepository = orderRepository;
		this.emailProvider = emailProvider;
		this.notificationService = notificationService;
		this.productService = productService;
		this.sseService = sseService;
		this.authService = authService;
	}


	@Transactional
	public String generateUrl(PaymentUrlRequestDTO input) {
		String gatewayCustomerId = getMyGatewayCustomerId();
		OrderDTO order = orderService.internalGet(input.orderId());

		return paymentGateway.generatePaymentUrl(
						gatewayCustomerId,
						order.total(),
						order.id()
		);
	}


	@Transactional
	public void handleAsaasWebhook(AsaasPayWebhook webhook) {
		//map as router handling
		switch (webhook.event()) {
			case PAYMENT_CONFIRMED, PAYMENT_RECEIVED, PAYMENT_APPROVED_BY_RISK_ANALYSIS:
				handlePaymentConfirmed(webhook);
				break;
			case PAYMENT_AWAITING_RISK_ANALYSIS:
				handlePaymentAwaitingRiskAnalysis(webhook);
				break;
			case PAYMENT_REPROVED_BY_RISK_ANALYSIS:
				handlePaymentReprovedByRiskAnalysis(webhook);
				break;
			default:
				log.info("Ignoring unknown event: " + webhook.event());
		}
	}

	private void handlePaymentReprovedByRiskAnalysis(AsaasPayWebhook webhook) {
		log.info("Handling PAYMENT_REPROVED_BY_RISK_ANALYSIS event");
		AsaasPayWebhook.Payment theEvent = webhook.payment();
		Long orderId = theEvent.externalReference();
		Order order = orderRepository.findById(orderId).orElseThrow();
		order.setStatus(OrderStatus.REPROVED);
		orderRepository.save(order);
		emailProvider.send(
						order.getClient().getEmail(),
						"Payment Service",
						"Payment Reproved",
						buildPaymentReprovedEmail(order)
		);
	}

	private String buildPaymentReprovedEmail(Order order) {
		//TODO: integrate with thymeleaf
		return "payment reproved email";
	}

	private void handlePaymentAwaitingRiskAnalysis(AsaasPayWebhook webhook) {
		log.info("Handling PAYMENT_AWAITING_RISK_ANALYSIS event");
		AsaasPayWebhook.Payment theEvent = webhook.payment();
		Long orderId = theEvent.externalReference();
		Order order = orderRepository.findById(orderId).orElseThrow();
		order.setStatus(OrderStatus.PROCESSING);
		orderRepository.save(order);
		emailProvider.send(
						order.getClient().getEmail(),
						"Payment Service",
						"We are processing your payment",
						buildProcessingPaymentEmail(order)
		);
	}

	private String buildProcessingPaymentEmail(Order order) {
		//TODO: integrate with thymeleaf
		return "processing payment email";
	}

	@Transactional()
	protected String getMyGatewayCustomerId() {
		UserDTO me = userService.getMe();
		Optional<String> gatewayCustomerId = paymentGateway.getGatewayCustomerIdByDb(me.id());

		if (gatewayCustomerId.isEmpty()) {
			log.info("Searching customer ID by HTTP API");
			gatewayCustomerId = paymentGateway.getGatewayCustomerIdByHttp(me);

			if (gatewayCustomerId.isEmpty()) {
				String createdGatewayCustomerId = paymentGateway.createCustomer(me);
				gatewayCustomerId = Optional.of(createdGatewayCustomerId);
				log.info("Created Gateway Customer ID: " + createdGatewayCustomerId);
			}
		} else {
			log.info("Customer ID found in DB");
		}

		return gatewayCustomerId.get();
	}

	@Transactional()
	protected String getMyGatewayCustomerId(UserDTO me) {
		Optional<String> gatewayCustomerId = paymentGateway.getGatewayCustomerIdByDb(me.id());

		if (gatewayCustomerId.isEmpty()) {
			log.info("Searching customer ID by HTTP API");
			gatewayCustomerId = paymentGateway.getGatewayCustomerIdByHttp(me);

			if (gatewayCustomerId.isEmpty()) {
				String createdGatewayCustomerId = paymentGateway.createCustomer(me);
				gatewayCustomerId = Optional.of(createdGatewayCustomerId);
				log.info("Created Gateway Customer ID: " + createdGatewayCustomerId);
			}
		} else {
			log.info("Customer ID found in DB");
		}

		return gatewayCustomerId.get();
	}


	@Transactional
	protected void handlePaymentConfirmed(AsaasPayWebhook webhook) {
		String eventName = webhook.event().toString();
		AsaasPayWebhook.Payment theEvent = webhook.payment();
		Long orderId = theEvent.externalReference();

		if (webhook.event() == AsaasPayWebhook.EventType.PAYMENT_RECEIVED
						&& BillingType.valueOf(theEvent.billingType()) == BillingType.CREDIT_CARD) {
			log.info("Ignoring PAYMENT_RECEIVED event for CREDIT_CARD due to handled by PAYMENT_CONFIRMED event");
			return;
		}
		if (BillingType.valueOf(theEvent.billingType()) == BillingType.PIX) {
			sseService.sendToSubscriber(orderId.toString(), "OK");
		}

		log.info("Handling " + eventName + " asaas webhook event");
		Order order = orderRepository.findById(orderId).orElseThrow();
		Payment payment = new Payment(
						order,
						BillingType.valueOf(theEvent.billingType()),
						theEvent.value(),
						theEvent.netValue()
		);
		order.setStatus(OrderStatus.PAID);
		order.setPayment(payment);
		order.getItems().forEach(item -> item.getProduct().setStock(item.getProduct().getStock() - item.getQuantity()));
		orderRepository.save(order);

		ConfirmationOrderMailInput mailDetails = new ConfirmationOrderMailInput(
						order.getClient().getEmail(),
						order.getClient().getName(),
						order.getItems().stream().map(item -> new ConfirmationOrderMailInput.Item(
										item.getProduct().getName(),
										item.getQuantity().toString(),
										item.getProduct().getPrice(),
										productService.toThumbUrl(item.getProduct().getThumbPath())
						)).toList(),
						order.getTotal()
		);
		emailProvider.sendOrderConfirmationEmail(mailDetails);
		notificationService.notifyListeners(webhook);

	}

	@Transactional
	public Object tokenizeCard(TokenizeCardRequestSecureDTO bodyDTO) {
		UserDTO me = userService.getMe();
		String gatewayCustomerId = getMyGatewayCustomerId(me);
		return bodyDTO.tokenizeWith(paymentGateway, gatewayCustomerId, me.id());

	}

	@Transactional
	public boolean processCheckout(ProcessCheckoutSecureDTO bodyDTO) {
		UserDTO me = userService.getMe();
		OrderDTO order = orderService.internalGet(bodyDTO.getDomainOrderId());
		log.info("Processing checkout for order: " + order.id());
		boolean success = bodyDTO.processWith(paymentGateway, me, order);
		return success;
	}

	@Transactional(readOnly = true)
	public SseEmitter ssePix(String orderId) {
		OrderDTO order = orderService.findById(Long.parseLong(orderId));
		authService.validateSelfOrAdmin(order.client().id());
		return sseService.addSubscriber(orderId);
	}

	public PaymentPixDetailsResponseDTO generatePixDetails(String orderId) {
		UserDTO me = userService.getMe();
		OrderDTO order = orderService.internalGet(Long.parseLong(orderId));
		log.info("paymentGateway.generatePixDetails");
		GetPaymentPixQRCodeResponse generated = paymentGateway.generatePixDetails(me, order);
		log.info("return new PaymentPixDetailsResponseDTO");
		return new PaymentPixDetailsResponseDTO(
						generated.getEncodedImage(),
						generated.getPayload(),
						generated.getExpirationDate()
		);
	}
}