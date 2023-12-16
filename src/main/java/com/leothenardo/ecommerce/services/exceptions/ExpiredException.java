package com.leothenardo.ecommerce.services.exceptions;

public class ExpiredException extends RuntimeException {
	public ExpiredException() {
		super("Token expired");
	}
}
