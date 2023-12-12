package com.leothenardo.ecommerce.dtos;

import com.leothenardo.ecommerce.models.OrderItem;

public record OrderItemDTO(Long productId,
													 String productName,
													 Integer quantity,
													 Double price,
													 Double subTotal,
													 String imgUrl) {

	//from model
	public static OrderItemDTO from(OrderItem entity) {
		Double subTotal = entity.getPrice() * entity.getQuantity();
		return new OrderItemDTO(
						entity.getProduct().getId(),
						entity.getProduct().getName(),
						entity.getQuantity(),
						entity.getPrice(),
						subTotal,
						null
		);
	}

}
