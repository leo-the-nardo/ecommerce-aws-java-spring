package com.leothenardo.ecommerce.services;

import com.leothenardo.ecommerce.dtos.ProductDTO;
import com.leothenardo.ecommerce.models.Product;
import com.leothenardo.ecommerce.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductService {
	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> result = productRepository.findById(id);
		Product productDb = result.get();
		return ProductDTO.from(productDb);
	}

	@Transactional(readOnly = true)
	public Page<ProductDTO> findAll(Pageable pageable) {
		Page<Product> resultDb = productRepository.findAll(pageable);
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
		Optional<Product> previousProduct = productRepository.findById(id);
		Product previousProductDb = previousProduct.get();
		Product product = new Product(
						id,
						productDTO.name() != null ? productDTO.name() : previousProductDb.getName(),
						productDTO.description() != null ? productDTO.description() : previousProductDb.getDescription(),
						productDTO.price() != null ? productDTO.price() : previousProductDb.getPrice(),
						productDTO.imgUrl() != null ? productDTO.imgUrl() : previousProductDb.getImgUrl());
		Product persistedProduct = productRepository.save(product);
		return ProductDTO.from(persistedProduct);
	}

	@Transactional
	public void delete(Long id) {
		productRepository.deleteById(id);
	}
}
