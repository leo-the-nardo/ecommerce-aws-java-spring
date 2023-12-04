package com.leothenardo.ecommerce.dtos;

import org.springframework.validation.FieldError;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ValidationError {
	private Instant timestamp;
	private Integer status;
	private String error;
	private List<FieldMessage> errors = new ArrayList<>();
	private String path;

	public ValidationError() {
	}

	public ValidationError(
					Instant timestamp,
					Integer status,
					String error,
					List<FieldError> errors,
					String path) {

		this.timestamp = timestamp;
		this.status = status;
		this.error = error;
		this.path = path;
		errors.forEach(e -> this.errors.add(
						new FieldMessage(e.getField(), e.getDefaultMessage())
		));
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public Integer getStatus() {
		return status;
	}

	public String getError() {
		return error;
	}

	public List<FieldMessage> getErrors() {
		return errors;
	}

	public String getPath() {
		return path;
	}

	private record FieldMessage(String fieldName, String message) {

	}

}
