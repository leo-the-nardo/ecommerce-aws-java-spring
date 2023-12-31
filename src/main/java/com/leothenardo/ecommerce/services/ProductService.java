package com.leothenardo.ecommerce.services;

import com.leothenardo.ecommerce.config.StorageProperties;
import com.leothenardo.ecommerce.dtos.*;
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

import java.util.*;

@Service
public class ProductService {
	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final FileReferenceRepository fileReferenceRepository;
	private final StorageService storageService;
	private final StorageProperties storageProperties;

	public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, FileReferenceRepository fileReferenceRepository, StorageService storageService, StorageProperties storageProperties) {
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
		this.fileReferenceRepository = fileReferenceRepository;
		this.storageService = storageService;
		this.storageProperties = storageProperties;
	}

	@Transactional(readOnly = true)
	public FindProductOutputDTO findById(Long id) {
		Product product = productRepository
						.findById(id)
						.orElseThrow(() -> new ResourceNotFoundException(id));
		List<String> imagesURL = new ArrayList<>();
		if (!product.getImages().isEmpty()) {
			product.getImages().forEach(image -> imagesURL.add(toImageUrl(image.getPath())));
		}
		return new FindProductOutputDTO(
						product.getId(),
						product.getName(),
						product.getPrice(),
						product.getDescription(),
						toThumbUrl(product.getThumbPath()),
						imagesURL,
						product.getCategories().stream().map(CategoryDTO::from).toList()
		);
	}

	@Transactional(readOnly = true)
	public Page<SearchProductMinResultDTO> search(String name, Pageable pageable) {
		Page<Product> resultDb = productRepository.searchByName(name, pageable);
		return resultDb.map(p -> new SearchProductMinResultDTO(
						p.getId(),
						p.getName(),
						p.getPrice(),
						toThumbUrl(p.getThumbPath())));
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
						categories,
						thumb.getPath()
		);

		product.getImages().forEach(image -> image.setTemp(false));
		if (product.getThumb() != null) product.getThumb().setTemp(false);

		Product persistedProduct = productRepository.save(product);
		return persistedProduct.getId();
	}

	@Transactional
	public void update(Long id, UpdateProductInputDTO productDTO) {
		Product previousProduct = productRepository
						.findById(id)
						.orElseThrow(() -> new ResourceNotFoundException(id));

		Set<Category> categories = productDTO.categoriesIds() != null && !productDTO.categoriesIds().isEmpty() ?
						new HashSet<>(productDTO.categoriesIds().stream().map(
										cId -> new Category(cId, null, null)).toList()
						)
						: previousProduct.getCategories();

		FileReference thumb = productDTO.thumbId() != null ?
						getThumbReference(productDTO.thumbId())
						: previousProduct.getThumb();

		List<FileReference> images = productDTO.imagesIds() != null ?
						getImagesReferences(productDTO.imagesIds())
						: previousProduct.getImages();

		Product product = new Product(
						id,
						productDTO.name() != null ? productDTO.name() : previousProduct.getName(),
						productDTO.description() != null ? productDTO.description() : previousProduct.getDescription(),
						productDTO.price() != null ? productDTO.price() : previousProduct.getPrice(),
						thumb,
						images,
						categories,
						thumb.getPath()
		);
		if (productDTO.thumbId() != null && !productDTO.thumbId().equals(previousProduct.getThumb().getId())) {
			previousProduct.getThumb().setTemp(true);
			storageService.softDelete(previousProduct.getThumb());
		}
		if (productDTO.imagesIds() != null) {
			previousProduct.getImages().forEach(image -> {
				if (!productDTO.imagesIds().contains(image.getId())) {
					image.setTemp(true);
					storageService.softDelete(image);
				}
			});
		}
		product.getImages().forEach(image -> image.setTemp(false));
		product.getThumb().setTemp(false);
		productRepository.save(product);
	}

	@Transactional
	public void softDelete(Long id) {
		Product product = productRepository.findById(id)
						.orElseThrow(() -> new ResourceNotFoundException(id));

		product.delete();
		product.getImages().forEach(image -> image.setTemp(true));
		product.getThumb().setTemp(true);
		storageService.softDelete(product.getThumb());
		product.getImages().forEach(storageService::softDelete);
		productRepository.save(product);
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
			throw new BusinessException(String.format("File %s is not an thumb", thumb.getId()));
		}
		boolean existsInStorage = storageService.fileExists(thumb);

		if (!existsInStorage) {
			throw new ResourceNotFoundException(String.format("File %s not exists on storage", thumb.getId()));
		}
		return thumb;
	}

	public String toThumbUrl(String thumbPath) {
		return storageProperties.getThumb().getDownloadUrl() + "/" + thumbPath;
	}

	private String toImageUrl(String imagePath) {
		if (imagePath == null) return null;
		return storageProperties.getImage().getDownloadUrl() + "/" + imagePath;
	}
}
