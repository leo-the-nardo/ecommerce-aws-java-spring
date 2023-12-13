package com.leothenardo.ecommerce.models;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "tb_product")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	@Column(columnDefinition = "TEXT")
	private String description;
	private Double price;
	private String thumbPath;

	private Instant deletedAt;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private FileReference thumb;

	// relation
	@OneToMany(cascade = CascadeType.ALL)
	private List<FileReference> images;

	@ManyToMany
	@JoinTable(name = "tb_product_category",
					joinColumns = @JoinColumn(name = "product_id"),
					inverseJoinColumns = @JoinColumn(name = "category_id"))
	private Set<Category> categories = new HashSet<>();

	@OneToMany(mappedBy = "id.product")
	private Set<OrderItem> orderItems = new HashSet<>();


//	public Product(Long id, String name, String description, Double price, List<FileReference> images, Set<Category> categories) {
//		this.id = id;
//		this.name = name;
//		this.description = description;
//		this.price = price;
//		this.imgUrl = imgUrl;
//		this.categories = categories;
//	}

	public Product(Long id,
								 String name,
								 String description,
								 Double price,
								 String thumbId,
								 List<String> imagesId,
								 Set<Long> categoriesId) {

		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.thumb = mapThumb(thumbId);
		this.categories = mapCategories(categoriesId);
		this.images = mapImages(imagesId);

	}

	public Product(
					String name,
					String description,
					Double price,
					FileReference thumb,
					List<FileReference> images,
					Set<Category> categories,
					String thumbPath
	) {

		this.name = name;
		this.description = description;
		this.price = price;
		this.thumb = thumb;
		this.categories = categories;
		this.images = images;
		this.thumbPath = thumbPath;

	}


	public Product() {
	}


	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FileReference getThumb() {
		return thumb;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getThumbPath() {
		return thumbPath;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public List<FileReference> getImages() {
		return images;
	}

	private List<FileReference> mapImages(List<String> imagesId) {
		if (imagesId == null || imagesId.isEmpty()) return new ArrayList<>();
		return imagesId.stream().map(
						imgId -> new FileReference(
										imgId,
										null,
										null,
										null,
										null,
										false,
										null
						)
		).toList();
	}

	//mapCategories
	private Set<Category> mapCategories(Set<Long> categoriesId) {
		return categoriesId.stream().map(
						categoryId -> new Category(categoryId, null, null)
		).collect(Collectors.toSet());
	}

	//mapThumb
	private FileReference mapThumb(String thumbId) {
		if (thumbId == null) return null;
		return new FileReference(
						thumbId,
						null,
						null,
						null,
						null,
						false,
						null
		);
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Product product = (Product) o;
		return Objects.equals(id, product.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	public void delete() {
		this.deletedAt = Instant.now();
	}
}
