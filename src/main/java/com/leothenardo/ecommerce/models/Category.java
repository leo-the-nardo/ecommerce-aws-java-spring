package com.leothenardo.ecommerce.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_category")
public class Category {

	@Id
	private String id;
	private String name;

	@ManyToMany(mappedBy = "categories")
	private Set<Product> products = new HashSet<>();


	public Category() {
	}

	public Category(String id, String name, Set<Product> products) {
		this.id = id;
		this.name = name;
		this.products = products;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}
}
