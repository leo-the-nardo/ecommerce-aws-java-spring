package com.leothenardo.ecommerce.models;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;

@Embeddable
public class OrderItemID implements Serializable {

	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;

	@JoinColumn(name = "product_id")
	@ManyToOne
	private Product product;

	public OrderItemID() {
	}

	public OrderItemID(Order order, Product product) {
		this.order = order;
		this.product = product;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
}
