package com.leothenardo.ecommerce.gateways.models.asaas;

public record PostTokenizeCardResponse(
				String creditCardNumber,
				String creditCardBrand,
				String creditCardToken
) {
}
