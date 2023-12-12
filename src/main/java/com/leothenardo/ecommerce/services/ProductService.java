package com.leothenardo.ecommerce.services;

import com.leothenardo.ecommerce.dtos.CreateProductInputDTO;
import com.leothenardo.ecommerce.dtos.ProductDTO;
import com.leothenardo.ecommerce.dtos.ProductMinDTO;
import com.leothenardo.ecommerce.models.Category;
import com.leothenardo.ecommerce.models.FileReference;
import com.leothenardo.ecommerce.models.Product;
import com.leothenardo.ecommerce.repositories.CategoryRepository;
import com.leothenardo.ecommerce.repositories.FileReferenceRepository;
import com.leothenardo.ecommerce.repositories.ProductRepository;
import com.leothenardo.ecommerce.services.exceptions.BusinessException;
import com.leothenardo.ecommerce.services.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProductService {
	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final FileReferenceRepository fileReferenceRepository;
	private final StorageService storageService;

	public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, FileReferenceRepository fileReferenceRepository, StorageService storageService) {
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
		this.fileReferenceRepository = fileReferenceRepository;
		this.storageService = storageService;
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
	public Long create(CreateProductInputDTO input) {
		FileReference thumb = getThumbReference(input.thumbId());
		List<FileReference> images = getImagesReferences(input.imagesIds());
		Set<Category> categories = getCategories(input.categoriesId());
		Product product = new Product(
						input.name(),
						input.description(),
						input.price(),
						thumb,
						images,
						categories
		);

		product.getImages().forEach(image -> image.setTemp(false));
		if (product.getThumb() != null) product.getThumb().setTemp(false);

		Product persistedProduct = productRepository.save(product);
		return persistedProduct.getId();
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

	private Set<Category> getCategories(Set<Long> categoriesId) {
		if (categoriesId == null || categoriesId.isEmpty()) {
			throw new BusinessException("At least one category is required");
		}
		categoriesId.forEach(id -> {
			if (!categoryRepository.existsById(id)) {
				throw new ResourceNotFoundException(id);
			}
		});
		return new HashSet<>(categoryRepository.findAllById(categoriesId));
	}

	private List<FileReference> getImagesReferences(List<String> imagesIds) {
		if (imagesIds == null || imagesIds.isEmpty()) {
			return new ArrayList<>();
		}

		List<FileReference> images = fileReferenceRepository.findAllById(imagesIds);
		images.forEach(image -> {
			if (!FileReference.Type.IMAGE.equals(image.getType())) {
				throw new BusinessException(String.format("File %s is not an image", image.getId()));
			}

			boolean existsInStorage = storageService.fileExists(image);
			if (!existsInStorage) {
				throw new ResourceNotFoundException(String.format("File %s not exists on storage", image.getId()));
			}

		});
		return images;
	}

	private FileReference getThumbReference(String id) {
		if (id == null) return null;
		FileReference thumb = fileReferenceRepository.findById(id)
						.orElseThrow(() -> new ResourceNotFoundException(id));
		if (!FileReference.Type.THUMB.equals(thumb.getType())) { //possible refactor
			throw new BusinessException(String.format("File %s is not an image", thumb.getId()));
		}
		boolean existsInStorage = storageService.fileExists(thumb);

		if (!existsInStorage) {
			throw new ResourceNotFoundException(String.format("File %s not exists on storage", thumb.getId()));
		}
		return thumb;
	}
}
