package com.leothenardo.ecommerce.dtos.sensitive;

import com.leothenardo.ecommerce.gateways.PaymentGatewayProvider;
import com.leothenardo.ecommerce.gateways.models.asaas.PostTokenizeCardRequest;

public final class TokenizeCardRequestDTO {
	private CreditCard creditCard;
	private CreditCardHolderInfo creditCardHolderInfo;
	private String remoteIp;

	public TokenizeCardRequestDTO(CreditCard creditCard, CreditCardHolderInfo creditCardHolderInfo, String remoteIp) {
		this.creditCard = creditCard;
		this.creditCardHolderInfo = creditCardHolderInfo;
		this.remoteIp = remoteIp;
	}

	public Object tokenizeWith(PaymentGatewayProvider gatewayProvider, String customerGatewayId, Long userId) {
		System.out.println("TokenizeCardRequestDTO.tokenizeWith");
		var creditCard = new PostTokenizeCardRequest.CreditCard.Builder()
						.withHolderName(this.creditCard.holderName)
						.withNumber(this.creditCard.number)
						.withExpiryMonth(this.creditCard.expiryMonth)
						.withExpiryYear(this.creditCard.expiryYear)
						.withCcv(this.creditCard.ccv)
						.build();
		var creditCardHolderInfo = new PostTokenizeCardRequest.CreditCardHolderInfo.Builder()
						.withName(this.creditCardHolderInfo.name)
						.withEmail(this.creditCardHolderInfo.email)
						.withCpfCnpj(this.creditCardHolderInfo.cpfCnpj)
						.withPostalCode(this.creditCardHolderInfo.postalCode)
						.withAddressNumber(this.creditCardHolderInfo.addressNumber)
						.withAddressComplement(this.creditCardHolderInfo.addressComplement)
						.withPhone(this.creditCardHolderInfo.phone)
						.withMobilePhone(this.creditCardHolderInfo.mobilePhone)
						.build();
		var requestBody = new PostTokenizeCardRequest.Builder()
						.withCustomer(customerGatewayId)
						.withCreditCard(creditCard)
						.withCreditCardHolderInfo(creditCardHolderInfo)
						.withRemoteIp(this.remoteIp)
						.build();
		return gatewayProvider.tokenizeCard(requestBody, customerGatewayId, userId);
	}

	@Override
	public String toString() {
		return "TokenizeCardRequestDTO{}";
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
