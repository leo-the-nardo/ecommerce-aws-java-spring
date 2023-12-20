package com.leothenardo.ecommerce.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

// Payment gateways details for a user should be stored here
@Entity
public class CustomerDetails {
	@Id
	private Long id; // Same as user id

	@MapsId
	@OneToOne(fetch = FetchType.LAZY)
	private User user;

	@OneToMany(mappedBy = "customerDetails", cascade = CascadeType.ALL)
	private List<AsaasTokenCard> asaasTokenCard = new ArrayList<>();
	@OneToMany(mappedBy = "customerDetails", cascade = CascadeType.ALL)
	private List<AsaasCustomer> asaasCustomer = new ArrayList<>();

	public CustomerDetails(Long id) {
		this.id = id;
	}

	public CustomerDetails() {
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<AsaasTokenCard> getAsaasTokenCard() {
		return asaasTokenCard;
	}

	public void setAsaasTokenCard(List<AsaasTokenCard> asaasTokenCard) {
		this.asaasTokenCard = asaasTokenCard;
	}

	public List<AsaasCustomer> getAsaasCustomer() {
		return asaasCustomer;
	}

	public void setAsaasCustomer(List<AsaasCustomer> asaasCustomer) {
		this.asaasCustomer = asaasCustomer;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
