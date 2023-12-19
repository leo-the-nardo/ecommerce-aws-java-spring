package com.leothenardo.ecommerce.services;

import com.leothenardo.ecommerce.dtos.OrderDTO;
import com.leothenardo.ecommerce.dtos.OrderItemDTO;
import com.leothenardo.ecommerce.models.*;
import com.leothenardo.ecommerce.repositories.OrderItemRepository;
import com.leothenardo.ecommerce.repositories.OrderRepository;
import com.leothenardo.ecommerce.repositories.ProductRepository;
import com.leothenardo.ecommerce.services.exceptions.ForbiddenException;
import com.leothenardo.ecommerce.services.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class OrderService {
	private static final Logger log = LoggerFactory.getLogger(OrderService.class);

	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;
	private final ProductRepository productRepository;
	private final UserService userService;
	private final AuthService authService;

	public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, ProductRepository productRepository, UserService userService, AuthService authService) {
		this.orderRepository = orderRepository;
		this.orderItemRepository = orderItemRepository;
		this.productRepository = productRepository;
		this.userService = userService;
		this.authService = authService;
	}

	@Transactional(readOnly = true)
	public OrderDTO findById(Long id) {
		Order order = orderRepository.findById(id).orElseThrow(() -> {
			log.info("Order not found: {}", id);
			return new ResourceNotFoundException(id);
		});
		try {
			authService.validateSelfOrAdmin(order.getClient().getId());
			log.info("User {} get order {}", userService.authenticated().getId(), id);
			return OrderDTO.from(order);

		} catch (ForbiddenException e) {
			log.info("Access denied for user {} to order {}", userService.authenticated().getId(), id);
			throw new ForbiddenException("Access denied");
		}
	}

	@Transactional
	public OrderDTO create(OrderDTO orderDTO) {
		User client = userService.authenticated();
		Order order = new Order(
						Instant.now(),
						client
		);
		for (OrderItemDTO itemDTO : orderDTO.items()) {
			Product product = productRepository.getReferenceById(itemDTO.productId());
			OrderItem item = new OrderItem(order, product, itemDTO.quantity(), product.getPrice());
			order.getItems().add(item);
		}
		order = orderRepository.save(order);
		orderItemRepository.saveAll(order.getItems());
		log.info("Order created: {}", order.getId());
		return OrderDTO.from(order);
	}

	@Transactional(readOnly = true)
	public OrderDTO internalGet(Long id) {
		Order order = orderRepository.findById(id).orElseThrow(() -> {
			log.info("Order not found: {}", id);
			return new ResourceNotFoundException(id);
		});
		return OrderDTO.from(order);
	}

}
