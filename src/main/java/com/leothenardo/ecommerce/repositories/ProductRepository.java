package com.leothenardo.ecommerce.repositories;

import com.leothenardo.ecommerce.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
