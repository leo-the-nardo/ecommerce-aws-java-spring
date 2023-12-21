package com.leothenardo.ecommerce.dtos.sensitive;


import com.leothenardo.ecommerce.dtos.OrderDTO;
import com.leothenardo.ecommerce.dtos.UserDTO;
import com.leothenardo.ecommerce.gateways.PaymentGatewayProvider;
import com.leothenardo.ecommerce.gateways.models.asaas.PostCreditCardPaymentRequest;
import com.leothenardo.ecommerce.gateways.models.asaas.builders.PostCreditCardPaymentRequestBuilder;

public final class ProcessCheckoutSecureDTO {
	private CreditCard creditCard;
	private CreditCardHolderInfo creditCardHolderInfo;
	private String remoteIp;
	private Long domainOrderId;
	private String domainTokenCardId;

	public ProcessCheckoutSecureDTO(CreditCard creditCard, CreditCardHolderInfo creditCardHolderInfo, String remoteIp, Long domainOrderId, String domainTokenCardId) {
		this.creditCard = creditCard;
		this.creditCardHolderInfo = creditCardHolderInfo;
		this.remoteIp = remoteIp;
		this.domainOrderId = domainOrderId;
		this.domainTokenCardId = domainTokenCardId;
	}


	public boolean processWith(PaymentGatewayProvider gatewayProvider, UserDTO user, OrderDTO orderDTO) {
		if (this.domainTokenCardId != null && !this.domainTokenCardId.isBlank()) {
			System.out.println("---- processing with token card ----");
			return gatewayProvider.processCheckout(user.id(), orderDTO, domainTokenCardId);
		}
		var creditCard = new PostCreditCardPaymentRequestBuilder.CreditCard(
						this.creditCard.holderName,
						this.creditCard.number,
						this.creditCard.expiryMonth,
						this.creditCard.expiryYear,
						this.creditCard.ccv
		);
		var creditCardHolderInfo = new PostCreditCardPaymentRequestBuilder.CreditCardHolderInfo(
						this.creditCardHolderInfo.name,
						this.creditCardHolderInfo.email,
						this.creditCardHolderInfo.cpfCnpj,
						this.creditCardHolderInfo.postalCode,
						this.creditCardHolderInfo.addressNumber,
						this.creditCardHolderInfo.addressComplement,
						this.creditCardHolderInfo.phone,
						this.creditCardHolderInfo.mobilePhone
		);
		PostCreditCardPaymentRequest requestBody = new PostCreditCardPaymentRequestBuilder()
						.billingType("CREDIT_CARD")
						.dueDate("2025-12-31")
						.value(orderDTO.total())
						.description("Pedido #" + orderDTO.id())
						.externalReference(orderDTO.id().toString())
						.creditCard(creditCard)
						.creditCardHolderInfo(creditCardHolderInfo)
						.build();
		System.out.println("---- processing with credit card ----");
		boolean success = gatewayProvider.processCheckout(requestBody, user);
		creditCard = null;
		creditCardHolderInfo = null;
		reset();
		return success;
	}

	public Long getDomainOrderId() {
		return domainOrderId;
	}

	public void reset() {
		this.creditCard = null;
		this.creditCardHolderInfo = null;
	}

	@Override
	public String toString() {
		return "";
	}

	private static class CreditCard {
		private String holderName;
		private String number;
		private String expiryMonth;
		private String expiryYear;
		private String ccv;

		public CreditCard(String holderName, String number, String expiryMonth, String expiryYear, String ccv) {
			this.holderName = holderName;
			this.number = number;
			this.expiryMonth = expiryMonth;
			this.expiryYear = expiryYear;
			this.ccv = ccv;
		}

	}

	private static class CreditCardHolderInfo {
		private String name;
		private String email;
		private String cpfCnpj;
		private String postalCode;
		private String addressNumber;
		private String addressComplement;
		private String phone;
		private String mobilePhone;

		public CreditCardHolderInfo(String name, String email, String cpfCnpj, String postalCode, String addressNumber, String addressComplement, String phone, String mobilePhone) {
			this.name = name;
			this.email = email;
			this.cpfCnpj = cpfCnpj;
			this.postalCode = postalCode;
			this.addressNumber = addressNumber;
			this.addressComplement = addressComplement;
			this.phone = phone;
			this.mobilePhone = mobilePhone;
		}
	}
}
