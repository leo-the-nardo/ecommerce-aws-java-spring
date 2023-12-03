package com.leothenardo.ecommerce.interceptors;

import com.leothenardo.ecommerce.dtos.CustomError;
import com.leothenardo.ecommerce.services.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}
