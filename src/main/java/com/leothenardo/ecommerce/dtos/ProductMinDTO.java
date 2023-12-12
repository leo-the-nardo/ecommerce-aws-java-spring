package com.leothenardo.ecommerce.dtos;

import com.leothenardo.ecommerce.models.Product;

public record ProductMinDTO(
				Long id,
				String name,
				Double price,
				String imgUrl
) {

	public static ProductMinDTO from(Product product) {
		return new ProductMinDTO(
						product.getId(),
						product.getName(),
						product.getPrice(),
						null);
	}
}
