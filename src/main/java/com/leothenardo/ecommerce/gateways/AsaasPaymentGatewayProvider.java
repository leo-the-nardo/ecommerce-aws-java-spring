package com.leothenardo.ecommerce.gateways;

import com.leothenardo.ecommerce.config.PaymentGatewayProperties;
import com.leothenardo.ecommerce.gateways.models.asaas.*;
import com.leothenardo.ecommerce.models.AsaasCustomer;
import com.leothenardo.ecommerce.models.User;
import com.leothenardo.ecommerce.repositories.AsaasCustomerRepository;
import com.leothenardo.ecommerce.services.exceptions.PaymentGatewayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Component
public class AsaasPaymentGatewayProvider implements PaymentGatewayProvider {
	private static final Logger log = LoggerFactory.getLogger(S3CloudStorageProvider.class);
	private final PaymentGatewayProperties gatewayProperties;
	private final AsaasCustomerRepository asaasCustomerRepository;
	private final WebClient webClient;

	public AsaasPaymentGatewayProvider(PaymentGatewayProperties gatewayProperties, AsaasCustomerRepository asaasCustomerRepository, WebClient.Builder webClientBuilder) {
		this.gatewayProperties = gatewayProperties;
		this.asaasCustomerRepository = asaasCustomerRepository;
		this.webClient = webClientBuilder.baseUrl(gatewayProperties.getAsaas().getApiUrl()).build();
	}

	@Override
	public String generatePaymentUrl(String customerId, double total, Long orderId) {
		Objects.requireNonNull(customerId);
		PostPaymentRequest body = new PostPaymentRequest.Builder()
						.withCustomer(customerId)
						.withDueDate("2025-07-07")
						.withValue(total)
						.withDescription("Pedido #" + orderId.toString())
						.withExternalReference(orderId.toString())
						.withBillingType("UNDEFINED")
						.build();
		try {
			PostPaymentResponse responseBody = webClient
							.post()
							.uri("/payments")
							.header("access_token", gatewayProperties.getAsaas().getApiKey())
							.bodyValue(body)
							.retrieve()
							.bodyToMono(PostPaymentResponse.class)
							.block();
			System.out.println(responseBody);
			System.out.println(responseBody.getInvoiceUrl());
			return responseBody.getInvoiceUrl();

		} catch (WebClientResponseException e) {
			System.out.println(e.getResponseBodyAsString());
			throw new PaymentGatewayException(e.getResponseBodyAsString());
		}
	}

	@Override
	public Optional<String> getGatewayCustomerIdByDb(Long domainUserId) {
		Optional<AsaasCustomer> customer = asaasCustomerRepository.findByUserId(domainUserId);
		return customer.map(AsaasCustomer::getId);
	}

	public Optional<String> getGatewayCustomerIdByHttp(User user) {
		String url = "/customers?email=" + user.getEmail() + "&externalReference=" + user.getId();
		try {
			FetchCustomersResponse response = webClient
							.get()
							.uri(url)
							.header("access_token", gatewayProperties.getAsaas().getApiKey())
							.retrieve()
							.bodyToMono(FetchCustomersResponse.class)
							.block();
			System.out.println(response);
			if (response.totalCount() == 0) {
				return Optional.empty();
			}
			return Arrays.stream(response.data()).map(CustomerDTO::id).findFirst();
		} catch (WebClientResponseException e) {
			log.error("Error on request to Asaas API: {}", e.getMessage());
			throw new PaymentGatewayException(e.getResponseBodyAsString());
		}
	}

	@Override
	public String createCustomer(User me) {
		PostCustomerRequest body = new PostCustomerRequest.Builder(me.getName(), me.getCpf())
						.email(me.getEmail())
						.externalReference(me.getId().toString())
						.build();
		try {
			PostCustomerResponse responseBody = webClient
							.post()
							.uri("/customers")
							.header("access_token", gatewayProperties.getAsaas().getApiKey())
							.bodyValue(body)
							.retrieve()
							.bodyToMono(PostCustomerResponse.class)
							.block();
			persistCustomer(responseBody.id(), me);
			return responseBody.id();
		} catch (WebClientResponseException e) {
			log.error("Error on request to Asaas API: {}", e.getMessage());
			throw new PaymentGatewayException(e.getResponseBodyAsString());
		}
	}


	public void persistCustomer(String gatewayCustomerId, User me) {
		AsaasCustomer customer = new AsaasCustomer(gatewayCustomerId, me);
		asaasCustomerRepository.save(customer);
	}

}
