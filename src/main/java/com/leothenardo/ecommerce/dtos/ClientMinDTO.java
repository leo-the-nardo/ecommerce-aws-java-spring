package com.leothenardo.ecommerce.dtos;

import com.leothenardo.ecommerce.models.User;

public record ClientMinDTO(Long id, String name) {
	public static ClientMinDTO from(User entity) {
		return new ClientMinDTO(
						entity.getId(),
						entity.getName());
	}
}
