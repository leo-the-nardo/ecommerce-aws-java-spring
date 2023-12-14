package com.leothenardo.ecommerce.services;

import com.leothenardo.ecommerce.models.User;
import com.leothenardo.ecommerce.services.exceptions.ForbiddenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class AuthService {
	private static final Logger log = LoggerFactory.getLogger(AuthService.class);
	private final UserService userService;

	public AuthService(UserService userService) {
		this.userService = userService;
	}

	public void validateSelfOrAdmin(Long userId) {
		User me = userService.authenticated();
		if (!me.hasRole("ROLE_ADMIN") && !me.getId().equals(userId)) {
			log.info("access denied user {} trying to access resource from user {}", me.getId(), userId);
			throw new ForbiddenException("Access denied");
		}

	}
}
