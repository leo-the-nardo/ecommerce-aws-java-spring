package com.leothenardo.ecommerce.gateways.models.asaas;


import com.leothenardo.ecommerce.gateways.models.asaas.builders.PostCreditCardPaymentRequestBuilder;

public class PostCreditCardPaymentRequest {
	private String customer;
	private String billingType;
	private String dueDate;
	private double value;
	private String description;
	private String externalReference;
	private PostCreditCardPaymentRequestBuilder.CreditCard creditCard;
	private PostCreditCardPaymentRequestBuilder.CreditCardHolderInfo creditCardHolderInfo;
	private String creditCardToken;

	public PostCreditCardPaymentRequest(PostCreditCardPaymentRequestBuilder builder) {
		this.customer = builder.getCustomer();
		this.billingType = builder.getBillingType();
		this.dueDate = builder.getDueDate();
		this.value = builder.getValue();
		this.description = builder.getDescription();
		this.externalReference = builder.getExternalReference();
		this.creditCard = builder.getCreditCard();
		this.creditCardHolderInfo = builder.getCreditCardHolderInfo();
		this.creditCardToken = builder.getCreditCardToken();
		builder.reset();
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getBillingType() {
		return billingType;
	}

	public String getDueDate() {
		return dueDate;
	}

	public double getValue() {
		return value;
	}

	public String getDescription() {
		return description;
	}

	public String getExternalReference() {
		return externalReference;
	}

	public PostCreditCardPaymentRequestBuilder.CreditCard getCreditCard() {
		return creditCard;
	}

	public PostCreditCardPaymentRequestBuilder.CreditCardHolderInfo getCreditCardHolderInfo() {
		return creditCardHolderInfo;
	}

	public String getCreditCardToken() {
		return creditCardToken;
	}

	@Override
	public String toString() {
		return "";
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
}
