package com.leothenardo.ecommerce.models;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;

@Entity
public class AsaasCustomer {
	@Id
	private String id;

	@JoinColumn(name = "customer_details_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private CustomerDetails customerDetails;


	@OneToMany(mappedBy = "asaasCustomer")
	private Set<AsaasTokenCard> cards;

	public AsaasCustomer() {
	}

	public AsaasCustomer(String id, CustomerDetails customerDetails) {
		Objects.requireNonNull(id);
		Objects.requireNonNull(customerDetails);
		this.id = id;
		this.customerDetails = customerDetails;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setCustomerDetails(CustomerDetails customerDetails) {
		this.customerDetails = customerDetails;
	}
}
