package com.leothenardo.ecommerce.gateways;

import com.leothenardo.ecommerce.models.User;

import java.util.Optional;

public interface PaymentGatewayProvider {

	void persistCustomer(String gatewayCustomerId, User me);

	String generatePaymentUrl(String gatewayCustomerId, double total, Long domainOrderId);

	Optional<String> getGatewayCustomerIdByDb(Long domainUserId);

	Optional<String> getGatewayCustomerIdByHttp(User user);

	String createCustomer(User me);

}
