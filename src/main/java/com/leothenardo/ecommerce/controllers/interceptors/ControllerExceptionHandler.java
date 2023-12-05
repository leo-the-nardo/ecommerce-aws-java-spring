package com.leothenardo.ecommerce.controllers.interceptors;

import com.leothenardo.ecommerce.dtos.CustomError;
import com.leothenardo.ecommerce.dtos.ValidationError;
import com.leothenardo.ecommerce.services.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<CustomError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
		var status = HttpStatus.NOT_FOUND;
		CustomError error = new CustomError(
						Instant.now(),
						status.value(),
						e.getMessage(),
						request.getRequestURI()
		);
		return ResponseEntity.status(status).body(error);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationError> validationDtoException(MethodArgumentNotValidException e, HttpServletRequest request) {
		var status = HttpStatus.UNPROCESSABLE_ENTITY;
		ValidationError error = new ValidationError(
						Instant.now(),
						status.value(),
						"Validation exception",
						e.getFieldErrors(),
						request.getRequestURI()
		);
		return ResponseEntity.status(status).body(error);
	}
}
