package com.leothenardo.ecommerce.dtos;

import com.leothenardo.ecommerce.models.Category;

public record CategoryDTO(
				Long id,
				String name
) {
	public static CategoryDTO from(Category category) {
		return new CategoryDTO(
						category.getId(),
						category.getName());
	}
}
