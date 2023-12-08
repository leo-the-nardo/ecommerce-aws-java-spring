package com.leothenardo.ecommerce.services;

import com.leothenardo.ecommerce.dtos.ProductDTO;
import com.leothenardo.ecommerce.dtos.ProductMinDTO;
import com.leothenardo.ecommerce.models.Category;
import com.leothenardo.ecommerce.models.Product;
import com.leothenardo.ecommerce.repositories.ProductRepository;
import com.leothenardo.ecommerce.services.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

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
	public Page<ProductMinDTO> search(String name, Pageable pageable) {
		Page<Product> resultDb = productRepository.searchByName(name, pageable);
		return resultDb.map(ProductMinDTO::from);
	}

	@Transactional
	public ProductDTO insert(ProductDTO productDTO) {
		Set<Category> categories = new HashSet<>(productDTO.categories().stream().map(
						categoryDTO -> new Category(categoryDTO.id(), null, null)).toList()
		);

		Product product = new Product(
						null,
						productDTO.name(),
						productDTO.description(),
						productDTO.price(),
						productDTO.imgUrl(),
						categories
		);
		productDTO.categories().forEach(category ->
						product.getCategories().add(new Category(category.id(), null, null))
		);

		Product persistedProduct = productRepository.save(product);
		System.out.println(persistedProduct.getCategories());
		return ProductDTO.from(persistedProduct);
	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO productDTO) {
		Product previousProduct = productRepository
						.findById(id)
						.orElseThrow(() -> new ResourceNotFoundException(id));

		Set<Category> newCategoriesOrPrevious = productDTO.categories() != null && !productDTO.categories().isEmpty() ?
						new HashSet<>(productDTO.categories().stream().map(
										categoryDTO -> new Category(categoryDTO.id(), null, null)).toList()
						)
						: previousProduct.getCategories();

		Product product = new Product(
						id,
						productDTO.name() != null ? productDTO.name() : previousProduct.getName(),
						productDTO.description() != null ? productDTO.description() : previousProduct.getDescription(),
						productDTO.price() != null ? productDTO.price() : previousProduct.getPrice(),
						productDTO.imgUrl() != null ? productDTO.imgUrl() : previousProduct.getImgUrl(),
						newCategoriesOrPrevious
		);

		Product persistedProduct = productRepository.save(product);
		return ProductDTO.from(persistedProduct);
	}

	@Transactional
	public void delete(Long id) {
		productRepository.deleteById(id);
	}
}
