package com.leothenardo.ecommerce.gateways;

import com.leothenardo.ecommerce.config.PaymentGatewayProperties;
import com.leothenardo.ecommerce.dtos.OrderDTO;
import com.leothenardo.ecommerce.dtos.UserDTO;
import com.leothenardo.ecommerce.gateways.models.asaas.*;
import com.leothenardo.ecommerce.gateways.models.asaas.builders.PostCreditCardPaymentRequestBuilder;
import com.leothenardo.ecommerce.models.AsaasCustomer;
import com.leothenardo.ecommerce.models.AsaasTokenCard;
import com.leothenardo.ecommerce.models.CustomerDetails;
import com.leothenardo.ecommerce.models.User;
import com.leothenardo.ecommerce.repositories.AsaasCustomerRepository;
import com.leothenardo.ecommerce.repositories.CustomerDetailsRepository;
import com.leothenardo.ecommerce.services.exceptions.PaymentGatewayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Objects;
import java.util.Optional;

@Component
public non-sealed class AsaasPaymentGatewayProvider implements PaymentGatewayProvider {
	private static final Logger log = LoggerFactory.getLogger(S3CloudStorageProvider.class);
	private final PaymentGatewayProperties gatewayProperties;
	private final AsaasCustomerRepository asaasCustomerRepository;
	private final WebClient webClient;
	private final CustomerDetailsRepository customerDetailsRepository;

	public AsaasPaymentGatewayProvider(PaymentGatewayProperties gatewayProperties, AsaasCustomerRepository asaasCustomerRepository, WebClient.Builder webClientBuilder, CustomerDetailsRepository customerDetailsRepository) {
		this.gatewayProperties = gatewayProperties;
		this.asaasCustomerRepository = asaasCustomerRepository;
		this.customerDetailsRepository = customerDetailsRepository;
		this.webClient = webClientBuilder
						.baseUrl(gatewayProperties.getAsaas().getApiUrl())
						.defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
						.defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE)
						.defaultHeader("access_token", gatewayProperties.getAsaas().getApiKey())
						.build();
	}

	@Override
	public String generatePaymentUrl(String customerId, double total, Long orderId) {
		Objects.requireNonNull(customerId);
		var body = new PostPaymentRequest.Builder()
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

	@Transactional(readOnly = true)
	@Override
	public Optional<String> getGatewayCustomerIdByDb(Long domainUserId) {
		Optional<AsaasCustomer> customer = asaasCustomerRepository.findByCustomerDetailsId(domainUserId);
		return customer.map(AsaasCustomer::getId);
	}


	@Transactional
	public Optional<String> getGatewayCustomerIdByHttp(UserDTO user) {
		String url = "/customers?email=" + user.email() + "&externalReference=" + user.id();
		try {
			FetchCustomersResponse response = webClient
							.get()
							.uri(url)
							.retrieve()
							.bodyToMono(FetchCustomersResponse.class)
							.block();
			System.out.println(response);
			if (response.totalCount() == 0) {
				return Optional.empty();
			}
			String customerId = response.data()[0].id();
			persistCustomer(customerId, user);
			return Optional.of(customerId);

		} catch (WebClientResponseException e) {
			log.error("Error on request to Asaas API: {}", e.getMessage());
			throw new PaymentGatewayException(e.getResponseBodyAsString());
		}
	}

	@Override
	@Transactional
	public String getGatewayCustomerId(UserDTO me) {
		Optional<String> gatewayCustomerId = getGatewayCustomerIdByDb(me.id());

		if (gatewayCustomerId.isEmpty()) {
			log.info("Searching customer ID by HTTP API");
			gatewayCustomerId = getGatewayCustomerIdByHttp(me);

			if (gatewayCustomerId.isEmpty()) {
				String createdGatewayCustomerId = createCustomer(me);
				gatewayCustomerId = Optional.of(createdGatewayCustomerId);
				log.info("Created Gateway Customer ID: " + createdGatewayCustomerId);
			}
		} else {
			log.info("Customer ID found in DB");
		}

		return gatewayCustomerId.get();
	}

	@Transactional
	@Override
	public String createCustomer(UserDTO me) {
		var body = new PostCustomerRequest.Builder(me.name(), me.cpf())
						.email(me.email())
						.externalReference(me.id().toString())
						.build();
		try {
			PostCustomerResponse responseBody = webClient
							.post()
							.uri("/customers")
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

	@Override
	@Transactional
	public String tokenizeCard(PostTokenizeCardRequest requestBody, String customerId, Long userId) {
		System.out.println(requestBody.toString());
		try {
			PostTokenizeCardResponse responseBody = webClient
							.post()
							.uri("/creditCard/tokenize")
							.bodyValue(requestBody)
							.retrieve()
							.bodyToMono(PostTokenizeCardResponse.class)
							.block();
			requestBody = null;

			CustomerDetails customerDetails = customerDetailsRepository.getReferenceById(userId);
			AsaasCustomer asaasCustomer = asaasCustomerRepository.getReferenceById(customerId);
			AsaasTokenCard asaasTokenCard = new AsaasTokenCard(
							responseBody.creditCardNumber(),
							responseBody.creditCardBrand(),
							responseBody.creditCardToken(),
							customerDetails,
							asaasCustomer);
			customerDetails.getAsaasTokenCard().add(asaasTokenCard);
			customerDetailsRepository.save(customerDetails);

			return responseBody.creditCardToken();

		} catch (WebClientResponseException e) {
			log.error("Error on request to Asaas API: {}", e.getMessage());
			throw new PaymentGatewayException(e.getResponseBodyAsString());
		}
	}

	@Override
	@Transactional
	public String persistTokenizedCard(Long userId, String asaasCustomerId, CreditCardDTO creditCardDTO) {
		CustomerDetails customerDetails = customerDetailsRepository.getReferenceById(userId);
		AsaasCustomer asaasCustomer = asaasCustomerRepository.getReferenceById(asaasCustomerId);

		AsaasTokenCard asaasTokenCard = new AsaasTokenCard(
						creditCardDTO.lastFourDigits(),
						creditCardDTO.creditCardBrand(),
						creditCardDTO.token(),
						customerDetails,
						asaasCustomer
		);
		customerDetails.getAsaasTokenCard().add(asaasTokenCard);
		customerDetailsRepository.save(customerDetails);
		return asaasTokenCard.getId();
	}


	@Override
	@Transactional(readOnly = true)
	public boolean processCheckout(Long userId, OrderDTO order, String cardTokenId) {
		CustomerDetails customerDetails = customerDetailsRepository.getReferenceById(userId);
		AsaasTokenCard asaasTokenCard = customerDetails.getAsaasTokenCard().stream().filter(
						asaasToken -> asaasToken.getId().equals(cardTokenId)
		).findFirst().orElseThrow();
		AsaasCustomer asaasCustomer = asaasTokenCard.getAsaasCustomer();

		PostCreditCardPaymentRequest requestBody = new PostCreditCardPaymentRequestBuilder()
						.customer(asaasCustomer.getId())
						.billingType("CREDIT_CARD")
						.creditCardToken(asaasTokenCard.getCreditCardToken())
						.externalReference(order.id().toString())
						.dueDate("2025-12-31")
						.description("Pedido #" + order.id())
						.value(order.total())
						.build();
		System.out.println(requestBody.toString());
		try {
			PostCreditCardPaymentResponse responseBody = webClient
							.post()
							.uri("/payments")
							.bodyValue(requestBody)
							.retrieve()
							.bodyToMono(PostCreditCardPaymentResponse.class)
							.block();
			System.out.println(responseBody);
			return true;

		} catch (WebClientResponseException e) {
			log.error("Error on process payment on Asaas API: {}", e.getResponseBodyAsString());
			return false;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public boolean processCheckout(PostCreditCardPaymentRequest requestBody, UserDTO user) {
		String gatewayCustomerId = getGatewayCustomerId(user);
		requestBody.setCustomer(gatewayCustomerId);

		try {
			PostCreditCardPaymentResponse responseBody = webClient
							.post()
							.uri("/payments")
							.bodyValue(requestBody)
							.retrieve()
							.bodyToMono(PostCreditCardPaymentResponse.class)
							.block();
			var creditCard = new CreditCardDTO(
							responseBody.getCreditCard().getCreditCardNumber(),
							responseBody.getCreditCard().getCreditCardBrand(),
							responseBody.getCreditCard().getCreditCardToken()
			);
			System.out.println("Persisting returned tokenized card from Asaas");
			persistTokenizedCard(user.id(), gatewayCustomerId, creditCard);

			return true;

		} catch (WebClientResponseException e) {
			log.error("Error on process payment on Asaas API: {}", e.getResponseBodyAsString());
			return false;
		}
	}

	@Transactional
	@Override
	public void persistCustomer(String gatewayCustomerId, UserDTO me) {
		log.info("Persisting customer ID on DB" + gatewayCustomerId);

		User user = new User();
		user.setId(me.id());

		CustomerDetails customerWithId = new CustomerDetails();
		customerWithId.setId(me.id());
		customerWithId.setUser(user);
		AsaasCustomer customer = new AsaasCustomer(gatewayCustomerId, customerWithId);
		customerWithId.getAsaasCustomer().add(customer);
		asaasCustomerRepository.save(customer);
		customerDetailsRepository.save(customerWithId);
	}

}
