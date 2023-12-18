package com.leothenardo.ecommerce.services;


import com.leothenardo.ecommerce.dtos.OrderDTO;
import com.leothenardo.ecommerce.dtos.PaymentUrlRequestDTO;
import com.leothenardo.ecommerce.gateways.PaymentGatewayProvider;
import com.leothenardo.ecommerce.models.User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class PaymentService {
	private static final Logger log = Logger.getLogger(PaymentService.class.getName());
	private final UserService userService;
	private final OrderService orderService;
	private final PaymentGatewayProvider paymentGateway;

	public PaymentService(UserService userService, OrderService orderService, PaymentGatewayProvider paymentGateway) {
		this.userService = userService;
		this.orderService = orderService;
		this.paymentGateway = paymentGateway;
	}


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
}
