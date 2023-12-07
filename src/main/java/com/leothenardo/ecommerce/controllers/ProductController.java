package com.leothenardo.ecommerce.controllers;

import com.leothenardo.ecommerce.dtos.ProductDTO;
import com.leothenardo.ecommerce.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
	public ResponseEntity<Page<ProductDTO>> fetch(
					@RequestParam(name = "name", defaultValue = "") String name,
					Pageable pageable) {

		Page<ProductDTO> paginatedDto = productService.search(name, pageable);
		return ResponseEntity.ok().body(paginatedDto);
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
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

	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@PutMapping(value = "/{id}")
	public ResponseEntity<ProductDTO> update(
					@Valid @PathVariable Long id,
					@RequestBody ProductDTO productDTO) {

		ProductDTO dto = productService.update(id, productDTO);
		return ResponseEntity.ok().body(dto);
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		productService.delete(id);
		return ResponseEntity.noContent().build();
	}
}