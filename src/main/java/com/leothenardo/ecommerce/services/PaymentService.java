package com.leothenardo.ecommerce.services;


import com.leothenardo.ecommerce.dtos.OrderDTO;
import com.leothenardo.ecommerce.dtos.PaymentUrlRequestDTO;
import com.leothenardo.ecommerce.dtos.mail.ConfirmationOrderMailInput;
import com.leothenardo.ecommerce.dtos.webhooks.AsaasPayWebhook;
import com.leothenardo.ecommerce.gateways.EmailProvider;
import com.leothenardo.ecommerce.gateways.PaymentGatewayProvider;
import com.leothenardo.ecommerce.models.*;
import com.leothenardo.ecommerce.repositories.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	public PaymentService(UserService userService, OrderService orderService, PaymentGatewayProvider paymentGateway, OrderRepository orderRepository, EmailProvider emailProvider, NotificationService notificationService, ProductService productService) {
		this.userService = userService;
		this.orderService = orderService;
		this.paymentGateway = paymentGateway;
		this.orderRepository = orderRepository;
		this.emailProvider = emailProvider;
		this.notificationService = notificationService;
		this.productService = productService;
	}


	@Transactional
	public String generateUrl(PaymentUrlRequestDTO input) {
		User me = userService.authenticated();
		Optional<String> gatewayCustomerId = getGatewayCustomerId(me);

		if (gatewayCustomerId.isEmpty()) {
			String createdGatewayCustomerId = paymentGateway.createCustomer(me);
			gatewayCustomerId = Optional.of(createdGatewayCustomerId);
			log.info("Created Gateway Customer ID: " + createdGatewayCustomerId);
		}
		OrderDTO order = orderService.internalGet(input.orderId());

		return paymentGateway.generatePaymentUrl(
						gatewayCustomerId.get(),
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

	private Optional<String> getGatewayCustomerId(User user) {
		Optional<String> gatewayCustomerId = paymentGateway.getGatewayCustomerIdByDb(user.getId());

		if (gatewayCustomerId.isEmpty()) {
			log.info("Searching customer ID by HTTP API");
			gatewayCustomerId = paymentGateway.getGatewayCustomerIdByHttp(user);

			if (gatewayCustomerId.isPresent()) {
				log.info("Persisting found customer ID by HTTP API: " + gatewayCustomerId.get());
				paymentGateway.persistCustomer(gatewayCustomerId.get(), user);
			}
		} else {
			log.info("Customer ID found in DB");
		}

		return gatewayCustomerId;
	}

	@Transactional
	protected void handlePaymentConfirmed(AsaasPayWebhook webhook) {
		String eventName = webhook.event().toString();
		AsaasPayWebhook.Payment theEvent = webhook.payment();
		if (webhook.event() == AsaasPayWebhook.EventType.PAYMENT_RECEIVED
						&& BillingType.valueOf(theEvent.billingType()) == BillingType.CREDIT_CARD) {
			log.info("Ignoring PAYMENT_RECEIVED event for CREDIT_CARD due to handled by PAYMENT_CONFIRMED event");
			return;
		}

		log.info("Handling " + eventName + " asaas webhook event");
		Long orderId = theEvent.externalReference();
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

}