package com.leothenardo.ecommerce.controllers;

import com.leothenardo.ecommerce.dtos.ProductDTO;
import com.leothenardo.ecommerce.dtos.UserDTO;
import com.leothenardo.ecommerce.services.ProductService;
import com.leothenardo.ecommerce.services.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
	@GetMapping(value = "/me")
	public ResponseEntity<UserDTO> findMe() {
		UserDTO dto = userService.getMe();
		return ResponseEntity.ok().body(dto);
	}

}
