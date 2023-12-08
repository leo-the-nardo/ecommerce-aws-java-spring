package com.leothenardo.ecommerce.services;

import com.leothenardo.ecommerce.dtos.CategoryDTO;
import com.leothenardo.ecommerce.dtos.ProductMinDTO;
import com.leothenardo.ecommerce.models.Category;
import com.leothenardo.ecommerce.models.Product;
import com.leothenardo.ecommerce.repositories.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {
	private CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll() {
		List<Category> resultDb = categoryRepository.findAll();
		return resultDb.stream().map(CategoryDTO::from).toList();
	}

}
