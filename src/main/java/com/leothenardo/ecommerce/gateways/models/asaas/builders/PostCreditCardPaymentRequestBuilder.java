package com.leothenardo.ecommerce.gateways.models.asaas.builders;

import com.leothenardo.ecommerce.gateways.models.asaas.PostCreditCardPaymentRequest;

public class PostCreditCardPaymentRequestBuilder {
	private String customer;
	private String billingType;
	private String dueDate;
	private double value;
	private String description;
	private String externalReference;
	private CreditCard creditCard;
	private CreditCardHolderInfo creditCardHolderInfo;
	private String creditCardToken;

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getBillingType() {
		return billingType;
	}

	public void setBillingType(String billingType) {
		this.billingType = billingType;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getExternalReference() {
		return externalReference;
	}

	public void setExternalReference(String externalReference) {
		this.externalReference = externalReference;
	}

	public CreditCard getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	public CreditCardHolderInfo getCreditCardHolderInfo() {
		return creditCardHolderInfo;
	}

	public void setCreditCardHolderInfo(CreditCardHolderInfo creditCardHolderInfo) {
		this.creditCardHolderInfo = creditCardHolderInfo;
	}

	public String getCreditCardToken() {
		return creditCardToken;
	}

	public void setCreditCardToken(String creditCardToken) {
		this.creditCardToken = creditCardToken;
	}

	public PostCreditCardPaymentRequestBuilder customer(String customer) {
		this.customer = customer;
		return this;
	}

	public PostCreditCardPaymentRequestBuilder billingType(String billingType) {
		this.billingType = billingType;
		return this;
	}

	public PostCreditCardPaymentRequestBuilder dueDate(String dueDate) {
		this.dueDate = dueDate;
		return this;
	}

	public PostCreditCardPaymentRequestBuilder value(double value) {
		this.value = value;
		return this;
	}

	public PostCreditCardPaymentRequestBuilder description(String description) {
		this.description = description;
		return this;
	}

	public PostCreditCardPaymentRequestBuilder externalReference(String externalReference) {
		this.externalReference = externalReference;
		return this;
	}

	public PostCreditCardPaymentRequestBuilder creditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
		return this;
	}

	public PostCreditCardPaymentRequestBuilder creditCardHolderInfo(CreditCardHolderInfo creditCardHolderInfo) {
		this.creditCardHolderInfo = creditCardHolderInfo;
		return this;
	}

	public PostCreditCardPaymentRequestBuilder creditCardToken(String creditCardToken) {
		this.creditCardToken = creditCardToken;
		return this;
	}

	public PostCreditCardPaymentRequest build() {
		return new PostCreditCardPaymentRequest(this);
	}

	public void reset() {
		this.customer = null;
		this.billingType = null;
		this.dueDate = null;
		this.value = 0;
		this.description = null;
		this.externalReference = null;
		this.creditCard = null;
		this.creditCardHolderInfo = null;
		this.creditCardToken = null;

	}

	public static class CreditCard {
		private String holderName;
		private String number;
		private String expiryMonth;
		private String expiryYear;
		private String ccv;

		// Constructor, getters, setters for CreditCard
		public CreditCard(String holderName, String number, String expiryMonth, String expiryYear, String ccv) {
			this.holderName = holderName;
			this.number = number;
			this.expiryMonth = expiryMonth;
			this.expiryYear = expiryYear;
			this.ccv = ccv;
		}

		public String getNumber() {
			return number;
		}

		public void setNumber(String number) {
			this.number = number;
		}

		public String getExpiryMonth() {
			return expiryMonth;
		}

		public void setExpiryMonth(String expiryMonth) {
			this.expiryMonth = expiryMonth;
		}

		public String getExpiryYear() {
			return expiryYear;
		}

		public void setExpiryYear(String expiryYear) {
			this.expiryYear = expiryYear;
		}

		public String getCcv() {
			return ccv;
		}

		public void setCcv(String ccv) {
			this.ccv = ccv;
		}

		public String getHolderName() {
			return holderName;
		}

		public void setHolderName(String holderName) {
			this.holderName = holderName;
		}

	}

	public static class CreditCardHolderInfo {
		private String name;
		private String email;
		private String cpfCnpj;
		private String postalCode;
		private String addressNumber;
		private String addressComplement;
		private String phone;
		private String mobilePhone;

		public CreditCardHolderInfo(String name, String email, String cpfCnpj, String postalCode,
																String addressNumber, String addressComplement, String phone, String mobilePhone) {
			this.name = name;
			this.email = email;
			this.cpfCnpj = cpfCnpj;
			this.postalCode = postalCode;
			this.addressNumber = addressNumber;
			this.addressComplement = addressComplement;
			this.phone = phone;
			this.mobilePhone = mobilePhone;

		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getCpfCnpj() {
			return cpfCnpj;
		}

		public void setCpfCnpj(String cpfCnpj) {
			this.cpfCnpj = cpfCnpj;
		}

		public String getPostalCode() {
			return postalCode;
		}

		public void setPostalCode(String postalCode) {
			this.postalCode = postalCode;
		}

		public String getAddressNumber() {
			return addressNumber;
		}

		public void setAddressNumber(String addressNumber) {
			this.addressNumber = addressNumber;
		}

		public String getAddressComplement() {
			return addressComplement;
		}

		public void setAddressComplement(String addressComplement) {
			this.addressComplement = addressComplement;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getMobilePhone() {
			return mobilePhone;
		}

		public void setMobilePhone(String mobilePhone) {
			this.mobilePhone = mobilePhone;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}
}
