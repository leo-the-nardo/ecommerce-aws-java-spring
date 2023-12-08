package com.leothenardo.ecommerce.repositories;

import com.leothenardo.ecommerce.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	
}
