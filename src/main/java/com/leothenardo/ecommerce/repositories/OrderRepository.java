package com.leothenardo.ecommerce.repositories;

import com.leothenardo.ecommerce.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}