package com.leothenardo.ecommerce.dtos;

import com.leothenardo.ecommerce.models.Category;
import com.leothenardo.ecommerce.models.Product;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record CreateProductInputDTO(
				Long id,

				@Size(min = 3, max = 80, message = "Name must be between 3 and 80 characters")
				@NotBlank(message = "Name is required")
				String name,

				@Min(value = 0, message = "Price must be greater than zero")
				@NotNull(message = "Price is required")
				Double price,

				@Size(min = 10, max = 300, message = "Description must be between 10 and 300 characters")
				String description,

				String thumbId,

				// from reference returned on upload
				List<String> imagesIds,

				@NotEmpty(message = "At least one category is required")
				Set<Long> categoriesId
) {
	//toEntity
	public Product toEntity() {
		List<String> imgsRemovedDupKeepsOrder = imagesIds.stream().distinct().collect(Collectors.toList());

		return new Product(
						null,
						name,
						description,
						price,
						thumbId,
						imgsRemovedDupKeepsOrder,
						categoriesId
		);
	}
}
