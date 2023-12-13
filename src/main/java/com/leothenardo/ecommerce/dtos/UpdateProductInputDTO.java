package com.leothenardo.ecommerce.dtos;

import java.util.List;

public record UpdateProductInputDTO(
				String name,
				String description,
				Double price,
				String thumbId,
				List<String> imagesIds,
				List<Long> categoriesIds
) {
}
