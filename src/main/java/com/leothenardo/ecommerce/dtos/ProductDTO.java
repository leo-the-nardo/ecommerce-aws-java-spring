package com.leothenardo.ecommerce.dtos;

import com.leothenardo.ecommerce.models.Product;

public record ProductDTO(Long id,
												 String name,
												 Double price,
												 String description,
												 String imgUrl) {

	public static ProductDTO from(Product product) {
		return new ProductDTO(
						product.getId(),
						product.getName(),
						product.getPrice(),
						product.getDescription(),
						product.getImgUrl());
	}
}
