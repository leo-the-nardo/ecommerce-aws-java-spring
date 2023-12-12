package com.leothenardo.ecommerce.dtos;

public class UploadRequestResultDTO {
	private final String fileReferenceId; //uuid
	private final String uploadSignedUrl;

	public UploadRequestResultDTO(String fileReferenceId, String uploadSignedUrl) {
		this.fileReferenceId = fileReferenceId;
		this.uploadSignedUrl = uploadSignedUrl;
	}

	public String getFileReferenceId() {
		return fileReferenceId;
	}

	public String getUploadSignedUrl() {
		return uploadSignedUrl;
	}
}
