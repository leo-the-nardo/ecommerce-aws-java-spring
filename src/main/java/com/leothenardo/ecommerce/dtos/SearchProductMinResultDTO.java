package com.leothenardo.ecommerce.dtos;


public record SearchProductMinResultDTO(
				Long id,
				String name,
				Double price,
				String thumbUrl
) {
}
