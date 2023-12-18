package com.leothenardo.ecommerce.controllers.interceptors;

import com.leothenardo.ecommerce.dtos.CustomError;
import com.leothenardo.ecommerce.dtos.ValidationError;
import com.leothenardo.ecommerce.services.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
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

	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<CustomError> forbidden(ForbiddenException e, HttpServletRequest request) {
		var status = HttpStatus.FORBIDDEN;
		CustomError error = new CustomError(
						Instant.now(),
						status.value(),
						e.getMessage(),
						request.getRequestURI()
		);
		return ResponseEntity.status(status).body(error);
	}

	@ExceptionHandler(ExpiredException.class)
	public ResponseEntity<CustomError> expired(ExpiredException e, HttpServletRequest request) {
		var status = HttpStatus.UNAUTHORIZED;
		CustomError error = new CustomError(
						Instant.now(),
						status.value(),
						e.getMessage(),
						request.getRequestURI()
		);
		return ResponseEntity.status(status).body(error);
	}

	@ExceptionHandler(AlreadyExists.class)
	public ResponseEntity<CustomError> alreadyExists(AlreadyExists e, HttpServletRequest request) {
		var status = HttpStatus.CONFLICT;
		CustomError error = new CustomError(
						Instant.now(),
						status.value(),
						e.getMessage(),
						request.getRequestURI()
		);
		return ResponseEntity.status(status).body(error);
	}

	@ExceptionHandler(PaymentGatewayException.class)
	public ResponseEntity<CustomError> paymentGatewayException(PaymentGatewayException e, HttpServletRequest request) {
		var status = HttpStatus.BAD_REQUEST;
		CustomError error = new CustomError(
						Instant.now(),
						status.value(),
						e.getMessage(),
						request.getRequestURI()
		);
		return ResponseEntity.status(status).body(error);
	}

}
