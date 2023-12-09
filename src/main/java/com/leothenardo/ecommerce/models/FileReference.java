package com.leothenardo.ecommerce.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
public class FileReference {
	@Id
	@Column(name = "id", columnDefinition = "char(36)")
	private String id;

	@CreationTimestamp
	private OffsetDateTime createdAt;

	private String name;

	private String contentType;

	private Long contentLength;

	private boolean temp = true;

	@Enumerated(EnumType.STRING)
	private Type type;


	public FileReference(String id, OffsetDateTime createdAt, String name, String contentType, Long contentLength, boolean temp, Type type) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(contentType);
		Objects.requireNonNull(contentLength);
		Objects.requireNonNull(type);

		this.id = id == null ? UUID.randomUUID().toString() : id;
		this.createdAt = createdAt;
		this.name = name;
		this.contentType = contentType;
		this.contentLength = contentLength;
		this.temp = temp;
		this.type = type;
	}

	public FileReference() {

	}

	public String getId() {
		return id;
	}

	public OffsetDateTime getCreatedAt() {
		return createdAt;
	}

	public String getName() {
		return name;
	}

	public String getContentType() {
		return contentType;
	}

	public Long getContentLength() {
		return contentLength;
	}

	public boolean isTemp() {
		return temp;
	}

	public void setTemp(boolean temp) {
		this.temp = temp;
	}

	public Type getType() {
		return type;
	}

	public boolean isPublicAccessible() {
		return this.type.publicAccessible;
	}

	/// INTERNAL CLASSIFICATION, IS NOT CONTENT-TYPE
	public enum Type {
		DOCUMENT(false),
		IMAGE(true);

		private final boolean publicAccessible;

		Type(boolean publicAccessible) {
			this.publicAccessible = publicAccessible;
		}
	}
}
