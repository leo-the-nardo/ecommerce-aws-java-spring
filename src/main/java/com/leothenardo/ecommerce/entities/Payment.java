package com.leothenardo.ecommerce.entities;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tb_payment")
public class Payment {

	@Id
	private String id;
	@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	private Instant moment;

	@OneToOne
	private Order order;

	public Payment() {
	}

	public Payment(Instant moment, Order order) {
		this.id = UUID.randomUUID().toString();
		this.moment = moment;
		this.order = order;
	}

	public Payment(String id, Instant moment, Order order) {
		this.id = id;
		this.moment = moment;
		this.order = order;
	}


	public String getId() {
		return id;
	}

	public Instant getMoment() {
		return moment;
	}

	public Order getOrder() {
		return order;
	}
}
