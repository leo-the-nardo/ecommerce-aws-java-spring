package com.leothenardo.ecommerce.services;

import com.leothenardo.ecommerce.dtos.ProductDTO;
import com.leothenardo.ecommerce.models.Product;
import com.leothenardo.ecommerce.repositories.ProductRepository;
import com.leothenardo.ecommerce.services.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Product productDb = productRepository
						.findById(id)
						.orElseThrow(() -> new ResourceNotFoundException(id));
		return ProductDTO.from(productDb);
	}

	@Transactional(readOnly = true)
	public Page<ProductDTO> search(String name, Pageable pageable) {
		Page<Product> resultDb = productRepository.searchByName(name, pageable);
		return resultDb.map(ProductDTO::from);
	}

	@Transactional
	public ProductDTO insert(ProductDTO productDTO) {
		Product product = new Product(
						null,
						productDTO.name(),
						productDTO.description(),
						productDTO.price(),
						productDTO.imgUrl());
		Product persistedProduct = productRepository.save(product);
		return ProductDTO.from(persistedProduct);
	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO productDTO) {
		Product previousProduct = productRepository
						.findById(id)
						.orElseThrow(() -> new ResourceNotFoundException(id));

		Product product = new Product(
						id,
						productDTO.name() != null ? productDTO.name() : previousProduct.getName(),
						productDTO.description() != null ? productDTO.description() : previousProduct.getDescription(),
						productDTO.price() != null ? productDTO.price() : previousProduct.getPrice(),
						productDTO.imgUrl() != null ? productDTO.imgUrl() : previousProduct.getImgUrl());
		Product persistedProduct = productRepository.save(product);
		return ProductDTO.from(persistedProduct);
	}

	@Transactional
	public void delete(Long id) {
		productRepository.deleteById(id);
	}
}
