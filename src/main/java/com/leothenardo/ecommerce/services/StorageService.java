package com.leothenardo.ecommerce.services;


import com.leothenardo.ecommerce.dtos.UploadRequestResultDTO;
import com.leothenardo.ecommerce.gateways.CloudStorageProvider;
import com.leothenardo.ecommerce.models.FileReference;
import com.leothenardo.ecommerce.repositories.FileReferenceRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class StorageService {
	private final CloudStorageProvider cloudStorageProvider;
	private final FileReferenceRepository fileReferenceRepository;

	public StorageService(CloudStorageProvider cloudStorageProvider, FileReferenceRepository fileReferenceRepository) {
		this.cloudStorageProvider = cloudStorageProvider;
		this.fileReferenceRepository = fileReferenceRepository;
	}

	public UploadRequestResultDTO generateUploadUrl(FileReference fileReference) {
		Objects.requireNonNull(fileReference);

		fileReferenceRepository.save(fileReference); // temporary reference
		URL presignedUploadUrl = cloudStorageProvider.generatePresignedUploadUrl(fileReference);
		return new UploadRequestResultDTO(fileReference.getId(), presignedUploadUrl.toString());
	}

	public boolean fileExists(FileReference fileReference) {
		Objects.requireNonNull(fileReference);
		return cloudStorageProvider.fileExists(fileReference.getPath());
	}

	public void softDelete(FileReference fileReference) {
		if (fileReference == null) {
			return;
		}
		cloudStorageProvider.moveFile(fileReference.getPath(), "deleted/" + fileReference.getPath());
	}

}
