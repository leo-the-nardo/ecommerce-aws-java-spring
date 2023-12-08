package com.leothenardo.ecommerce.dtos;

import com.leothenardo.ecommerce.models.Order;
import com.leothenardo.ecommerce.models.OrderItem;
import com.leothenardo.ecommerce.models.OrderStatus;
import jakarta.validation.constraints.NotEmpty;

import java.time.Instant;
import java.util.List;

public record OrderDTO(
				Long id,
				Instant moment,
				OrderStatus orderStatus,
				ClientMinDTO client,
				PaymentDTO payment,

				@NotEmpty(message = "should have at least one item")
				List<OrderItemDTO> items,
				Double total
) {
	public static OrderDTO from(Order orderEntity) {
		List<OrderItemDTO> itemsToDto = orderEntity.getItems().stream().map(OrderItemDTO::from).toList();
		Double total = itemsToDto.stream().mapToDouble(OrderItemDTO::subTotal).sum();
		return new OrderDTO(
						orderEntity.getId(),
						orderEntity.getMoment(),
						orderEntity.getStatus(),
						ClientMinDTO.from(orderEntity.getClient()),
						orderEntity.getPayment() == null ? null : PaymentDTO.from(orderEntity.getPayment()),
						itemsToDto,
						total
		);
	}
}
