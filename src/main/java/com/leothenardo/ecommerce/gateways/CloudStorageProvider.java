package com.leothenardo.ecommerce.gateways;

import com.leothenardo.ecommerce.models.FileReference;

import java.net.URL;

public interface CloudStorageProvider {
	URL generatePresignedUploadUrl(FileReference fileReference);

	boolean fileExists(String filePath);

	void moveFileAsDelete(String fromPath, String toPath);
}
