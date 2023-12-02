package com.leothenardo.ecommerce.entities;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tb_order")
public class Order {
	@Id
	private String id;
	@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	private Instant moment;
	private OrderStatus status;

	@ManyToOne
	@JoinColumn(name = "client_id")
	private User client;

	@OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
	private Payment payment;


	public Order(Instant moment, OrderStatus status, User client) {
		this.id = UUID.randomUUID().toString();
		this.moment = moment;
		this.status = status;
		this.client = client;
	}

	public Order(String id, Instant moment, OrderStatus status, User client, Payment payment) {
		this.id = id;
		this.moment = moment;
		this.status = status;
		this.client = client;
		this.payment = payment;
	}
	
	public Order() {
	}


	public String getId() {
		return id;
	}

	public Instant getMoment() {
		return moment;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public User getClient() {
		return client;
	}
}
