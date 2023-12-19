package com.leothenardo.ecommerce.models;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tb_payment")
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	//	@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	private Instant moment;

	private BillingType billingType;

	private double value;

	private double netValue;

	@OneToOne
	@MapsId
	private Order order;

	public Payment() {
	}

	public Payment(Long id, Instant moment, Order order) {
		this.id = id;
		this.moment = moment;
		this.order = order;
	}


	public Payment(Order order, BillingType billingType, double value, double netValue) {
		this.moment = Instant.now();
		this.order = order;
		this.billingType = billingType;
		this.value = value;
		this.netValue = netValue;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Instant getMoment() {
		return moment;
	}

	public void setMoment(Instant moment) {
		this.moment = moment;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Payment payment = (Payment) o;
		return Objects.equals(id, payment.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}