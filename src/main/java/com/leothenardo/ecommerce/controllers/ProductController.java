package com.leothenardo.ecommerce.controllers;

import com.leothenardo.ecommerce.dtos.CustomError;
import com.leothenardo.ecommerce.dtos.ProductDTO;
import com.leothenardo.ecommerce.services.ProductService;
import com.leothenardo.ecommerce.services.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.Instant;


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
	public ResponseEntity<ProductDTO> create(@Valid @RequestBody ProductDTO productDTO) {
		ProductDTO dto = productService.insert(productDTO);
		URI uri = ServletUriComponentsBuilder
						.fromCurrentRequestUri()
						.path("/{id}")
						.buildAndExpand(dto.id())
						.toUri();
		return ResponseEntity.created(uri).body(dto);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<ProductDTO> update(
					@Valid @PathVariable Long id,
					@RequestBody ProductDTO productDTO) {
		
		ProductDTO dto = productService.update(id, productDTO);
		return ResponseEntity.ok().body(dto);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		productService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
