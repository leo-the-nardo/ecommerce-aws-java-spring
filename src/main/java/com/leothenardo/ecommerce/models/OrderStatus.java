package com.leothenardo.ecommerce.models;

public enum OrderStatus {
	WAITING_PAYMENT,
	PROCESSING,
	PAID,
	SHIPPED,
	DELIVERED,
	CANCELED;
}
