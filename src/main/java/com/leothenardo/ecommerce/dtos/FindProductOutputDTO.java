package com.leothenardo.ecommerce.dtos;

import com.leothenardo.ecommerce.models.Product;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;

public record FindProductOutputDTO(
				Long id,

				@Size(min = 3, max = 80, message = "Name must be between 3 and 80 characters")
				@NotBlank(message = "Name is required")
				String name,

				@Min(value = 0, message = "Price must be greater than zero")
				@NotNull(message = "Price is required")
				Double price,

				@Size(min = 10, max = 300, message = "Description must be between 10 and 300 characters")
				String description,

				@NotBlank(message = "thumbUrl is required")
				String thumbUrl,

				List<String> imagesUrls,

				@NotEmpty(message = "At least one category is required")
				List<CategoryDTO> categories
) {
}
