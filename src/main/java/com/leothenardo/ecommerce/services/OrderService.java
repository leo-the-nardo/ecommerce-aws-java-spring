package com.leothenardo.ecommerce.services;

import com.leothenardo.ecommerce.dtos.OrderDTO;
import com.leothenardo.ecommerce.models.Order;
import com.leothenardo.ecommerce.repositories.OrderRepository;
import com.leothenardo.ecommerce.services.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
	private final OrderRepository orderRepository;

	public OrderService(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@Transactional(readOnly = true)
	public OrderDTO findById(Long id) {
		Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
		return OrderDTO.from(order);
	}
}
