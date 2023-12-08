package com.leothenardo.ecommerce.services;

import com.leothenardo.ecommerce.models.User;
import com.leothenardo.ecommerce.services.exceptions.ForbiddenException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
	private final UserService userService;

	public AuthService(UserService userService) {
		this.userService = userService;
	}

	public void validateSelfOrAdmin(Long userId) {
		User me = userService.authenticated();
		if (!me.hasRole("ROLE_ADMIN") && !me.getId().equals(userId)) {
			throw new ForbiddenException("Access denied");
		}

	}
}
