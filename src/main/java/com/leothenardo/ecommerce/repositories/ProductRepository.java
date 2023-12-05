package com.leothenardo.ecommerce.repositories;

import com.leothenardo.ecommerce.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {
	
	@Query("SELECT obj FROM Product obj WHERE LOWER(obj.name) LIKE LOWER(CONCAT('%', :name, '%'))")
	Page<Product> searchByName(String name, Pageable pageable);

}
