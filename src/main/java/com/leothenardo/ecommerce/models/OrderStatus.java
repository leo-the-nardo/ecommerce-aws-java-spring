package com.leothenardo.ecommerce.models;

public enum OrderStatus {
	WAITING_PAYMENT,
	PROCESSING,
	REPROVED,
	PAID,
	SHIPPED,
	DELIVERED,
	CANCELED;
}
