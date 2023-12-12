package com.leothenardo.ecommerce.dtos;

import com.leothenardo.ecommerce.models.FileReference;
import com.leothenardo.ecommerce.validation.AllowedContentTypes;
import com.leothenardo.ecommerce.validation.AllowedFileExtensions;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UploadThumbRequestDTO {
	@NotBlank
	@AllowedFileExtensions({"png", "jpg"})
	private String fileName;

	@NotBlank
	@AllowedContentTypes({"image/jpg", "image/png"})
	private String contentType;
	
	@NotNull
	@Min(1)
	@Max(1500000) //1,5mb
	private Long contentLength;

	public String getFileName() {
		return fileName;
	}

	public String getContentType() {
		return contentType;
	}

	public Long getContentLength() {
		return contentLength;
	}

	public FileReference toDomain() {
		return new FileReference(
						null,
						null,
						fileName,
						contentType,
						contentLength,
						true,
						FileReference.Type.THUMB
		);
	}
}
