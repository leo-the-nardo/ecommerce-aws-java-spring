package com.leothenardo.ecommerce.controllers;


import com.leothenardo.ecommerce.dtos.UploadImageRequestDTO;
import com.leothenardo.ecommerce.dtos.UploadRequestResultDTO;
import com.leothenardo.ecommerce.services.StorageService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/uploads")
public class UploadController {
	private final StorageService storageService;

	public UploadController(StorageService storageService) {
		this.storageService = storageService;
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@PostMapping("/images")
	public UploadRequestResultDTO newImageUploadRequest(
					@RequestBody @Valid UploadImageRequestDTO request
	) {
		return storageService.generateUploadUrl(request.toDomain());
	}
}
