package com.leothenardo.ecommerce.controllers;

import com.leothenardo.ecommerce.dtos.ProductDTO;
import com.leothenardo.ecommerce.services.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


@RestController
@RequestMapping(value = "/products")
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<ProductDTO> find(@PathVariable Long id) {
		ProductDTO dto = productService.findById(id);
		return ResponseEntity.ok().body(dto);
	}

	@GetMapping(value = "/")
	public ResponseEntity<Page<ProductDTO>> findAll(Pageable pageable) {
		Page<ProductDTO> paginatedDto = productService.findAll(pageable);
		return ResponseEntity.ok().body(paginatedDto);
	}

	@PostMapping(value = "/")
	public ResponseEntity<ProductDTO> create(@RequestBody ProductDTO productDTO) {
		ProductDTO dto = productService.insert(productDTO);
		URI uri = ServletUriComponentsBuilder
						.fromCurrentRequestUri()
						.path("/{id}")
						.buildAndExpand(dto.id())
						.toUri();
		return ResponseEntity.created(uri).body(dto);
	}
}
