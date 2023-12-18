package com.leothenardo.ecommerce.models;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class AsaasCustomer {
	@Id
	private String id;
	@JoinColumn(name = "user_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	public AsaasCustomer() {
	}

	public AsaasCustomer(String id, User user) {
		Objects.requireNonNull(id);
		Objects.requireNonNull(user);
		this.id = id;
		this.user = user;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
