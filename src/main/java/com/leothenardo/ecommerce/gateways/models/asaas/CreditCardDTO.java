package com.leothenardo.ecommerce.gateways.models.asaas;

public record CreditCardDTO(
				String lastFourDigits,
				String creditCardBrand,
				String token
) {
}
