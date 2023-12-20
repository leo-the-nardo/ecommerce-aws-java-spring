package com.leothenardo.ecommerce.gateways;

import com.leothenardo.ecommerce.dtos.UserDTO;
import com.leothenardo.ecommerce.gateways.models.asaas.PostTokenizeCardRequest;

import java.util.Optional;

public sealed interface PaymentGatewayProvider permits AsaasPaymentGatewayProvider {

	void persistCustomer(String gatewayCustomerId, UserDTO me);

	String generatePaymentUrl(String gatewayCustomerId, double total, Long domainOrderId);

	Optional<String> getGatewayCustomerIdByDb(Long domainUserId);

	Optional<String> getGatewayCustomerIdByHttp(UserDTO user);

	String createCustomer(UserDTO me);

	String tokenizeCard(PostTokenizeCardRequest requestBody, String customerId, Long userId);


}