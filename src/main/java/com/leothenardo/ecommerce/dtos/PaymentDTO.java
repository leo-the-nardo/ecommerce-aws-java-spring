package com.leothenardo.ecommerce.dtos;

import com.leothenardo.ecommerce.models.Payment;

import java.time.Instant;

public record PaymentDTO(Long id, Instant moment) {
	public static PaymentDTO from(Payment entity) {
		return new PaymentDTO(
						entity.getId(),
						entity.getMoment());
	}
}
