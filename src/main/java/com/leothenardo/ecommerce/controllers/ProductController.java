package com.leothenardo.ecommerce.controllers;

import com.leothenardo.ecommerce.dtos.ProductDTO;
import com.leothenardo.ecommerce.services.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping(value = "/{id}")
	public ProductDTO find(@PathVariable Long id) {
		return productService.findById(id);
	}
}
