package com.leothenardo.ecommerce.repositories;

import com.leothenardo.ecommerce.models.OrderItem;
import com.leothenardo.ecommerce.models.OrderItemID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemID> {

}
