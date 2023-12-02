package com.leothenardo.ecommerce.models;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_order_item")
public class OrderItem {
	@EmbeddedId
	private OrderItemID id;
	private Integer quantity;
	private Double price;

	public OrderItem() {
	}

	public OrderItem(Order order, Product product, Integer quantity, Double price) {
		this.id = new OrderItemID(order, product);
		this.quantity = quantity;
		this.price = price;
	}

	public Order getOrder() {
		return id.getOrder();
	}

	public Product getProduct() {
		return id.getProduct();
	}
}
