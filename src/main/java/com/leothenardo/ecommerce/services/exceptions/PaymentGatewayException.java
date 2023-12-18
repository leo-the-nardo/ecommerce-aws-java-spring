package com.leothenardo.ecommerce.services.exceptions;

public class PaymentGatewayException extends RuntimeException {
	public PaymentGatewayException(String message) {
		super(message);
	}
}
