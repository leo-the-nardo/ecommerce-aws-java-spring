package com.leothenardo.ecommerce.dtos;

import jakarta.validation.constraints.*;

import java.util.List;
import java.util.Set;

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

}
