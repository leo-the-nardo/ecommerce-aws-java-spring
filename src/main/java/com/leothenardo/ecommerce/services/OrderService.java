package com.leothenardo.ecommerce.services;

import com.leothenardo.ecommerce.dtos.OrderDTO;
import com.leothenardo.ecommerce.dtos.OrderItemDTO;
import com.leothenardo.ecommerce.models.*;
import com.leothenardo.ecommerce.repositories.OrderItemRepository;
import com.leothenardo.ecommerce.repositories.OrderRepository;
import com.leothenardo.ecommerce.repositories.ProductRepository;
import com.leothenardo.ecommerce.services.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class OrderService {
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
		authService.validateSelfOrAdmin(id); // If it's not admin or the user itself, it will throw an exception
		Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
		return OrderDTO.from(order);
	}

	@Transactional
	public OrderDTO create(OrderDTO orderDTO) {
		Order order = new Order();
		order.setMoment(Instant.now());
		order.setStatus(OrderStatus.WAITING_PAYMENT);
		User client = userService.authenticated();
		order.setClient(client);

		for (OrderItemDTO itemDTO : orderDTO.items()) {
			Product product = productRepository.getReferenceById(itemDTO.productId());
			OrderItem item = new OrderItem(order, product, itemDTO.quantity(), product.getPrice());
			order.getItems().add(item);
		}
		order = orderRepository.save(order);
		orderItemRepository.saveAll(order.getItems());
		return OrderDTO.from(order);
	}
}
