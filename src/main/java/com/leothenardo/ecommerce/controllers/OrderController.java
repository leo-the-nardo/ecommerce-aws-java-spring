package com.leothenardo.ecommerce.controllers;

import com.leothenardo.ecommerce.dtos.OrderDTO;
import com.leothenardo.ecommerce.services.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/orders")
public class OrderController {

	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping(value = "/{id}")
	public OrderDTO findById(@PathVariable Long id) {
		return orderService.findById(id);
	}
}
