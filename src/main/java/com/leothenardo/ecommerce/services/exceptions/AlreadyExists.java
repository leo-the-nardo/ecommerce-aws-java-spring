package com.leothenardo.ecommerce.services.exceptions;

public class AlreadyExists extends RuntimeException {
	public AlreadyExists(String uniqueField) {
		super("The unique-value " + uniqueField + " already exists.");
	}
}
