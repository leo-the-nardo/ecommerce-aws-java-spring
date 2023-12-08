package com.leothenardo.ecommerce.models;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Objects;

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

	public OrderItemID getId() {
		return id;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public Double getPrice() {
		return price;
	}

	public Order getOrder() {
		return id.getOrder();
	}

	public Product getProduct() {
		return id.getProduct();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		OrderItem orderItem = (OrderItem) o;
		return Objects.equals(id, orderItem.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
