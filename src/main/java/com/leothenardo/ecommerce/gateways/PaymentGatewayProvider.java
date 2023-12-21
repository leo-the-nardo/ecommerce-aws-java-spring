package com.leothenardo.ecommerce.gateways;

import com.leothenardo.ecommerce.dtos.OrderDTO;
import com.leothenardo.ecommerce.dtos.UserDTO;
import com.leothenardo.ecommerce.gateways.models.asaas.CreditCardDTO;
import com.leothenardo.ecommerce.gateways.models.asaas.PostCreditCardPaymentRequest;
import com.leothenardo.ecommerce.gateways.models.asaas.PostTokenizeCardRequest;

import java.util.Optional;

public sealed interface PaymentGatewayProvider permits AsaasPaymentGatewayProvider {

	void persistCustomer(String gatewayCustomerId, UserDTO me);

	String generatePaymentUrl(String gatewayCustomerId, double total, Long domainOrderId);

	Optional<String> getGatewayCustomerIdByDb(Long domainUserId);

	Optional<String> getGatewayCustomerIdByHttp(UserDTO user);

	String getGatewayCustomerId(UserDTO me);

	String createCustomer(UserDTO me);

	String tokenizeCard(PostTokenizeCardRequest requestBody, String customerId, Long userId);

	boolean processCheckout(Long userId, OrderDTO order, String cardTokenId);

	boolean processCheckout(PostCreditCardPaymentRequest requestBody, UserDTO user);

	String persistTokenizedCard(Long userId, String asaasCustomerId, CreditCardDTO creditCardDTO);

}