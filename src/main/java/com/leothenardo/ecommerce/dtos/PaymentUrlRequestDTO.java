package com.leothenardo.ecommerce.dtos;

import jakarta.validation.constraints.NotBlank;

public record PaymentUrlRequestDTO(
				@NotBlank String successUrl,
				@NotBlank Long orderId
) {
}