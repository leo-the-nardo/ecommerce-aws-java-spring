package com.leothenardo.ecommerce.dtos;

import com.leothenardo.ecommerce.models.Product;

public record SearchProductMinResultDTO(
				Long id,
				String name,
				Double price,
				String thumbUrl
) {
}
