package com.leothenardo.ecommerce.models;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class AsaasTokenCard {

	@Id
	private String id;
	private String lastFourDigits;
	private String creditCardBrand;
	private String creditCardToken;
	@ManyToOne
	@JoinColumn(name = "customer_details_id")
	private CustomerDetails customerDetails;
	@ManyToOne
	@JoinColumn(name = "asaas_customer_id")
	private AsaasCustomer asaasCustomer;

	public AsaasTokenCard(String lastFourDigits, String creditCardBrand, String creditCardToken, CustomerDetails customerDetails, AsaasCustomer asaasCustomer) {
		this.id = UUID.randomUUID().toString();
		this.lastFourDigits = lastFourDigits;
		this.creditCardBrand = creditCardBrand;
		this.creditCardToken = creditCardToken;
		this.customerDetails = customerDetails;
		this.asaasCustomer = asaasCustomer;
	}

	public AsaasTokenCard() {
	}

	public String getLastFourDigits() {
		return lastFourDigits;
	}

	public void setLastFourDigits(String lastFourDigits) {
		this.lastFourDigits = lastFourDigits;
	}

	public String getCreditCardBrand() {
		return creditCardBrand;
	}

	public void setCreditCardBrand(String creditCardBrand) {
		this.creditCardBrand = creditCardBrand;
	}

	public String getCreditCardToken() {
		return creditCardToken;
	}

	public void setCreditCardToken(String creditCardToken) {
		this.creditCardToken = creditCardToken;
	}

	public CustomerDetails getCustomerDetails() {
		return customerDetails;
	}

	public void setCustomerDetails(CustomerDetails customerDetails) {
		this.customerDetails = customerDetails;
	}

	public AsaasCustomer getAsaasCustomer() {
		return asaasCustomer;
	}

	public void setAsaasCustomer(AsaasCustomer asaasCustomer) {
		this.asaasCustomer = asaasCustomer;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
