package com.leothenardo.ecommerce.gateways;


import com.leothenardo.ecommerce.config.StorageProperties;
import com.leothenardo.ecommerce.models.FileReference;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.awscore.AwsRequestOverrideConfiguration;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URL;
import java.time.Duration;

@Component
public class S3CloudStorageProvider implements CloudStorageProvider {

	private final S3Presigner s3Presigner;
	private final S3Client s3Client;
	private final StorageProperties storageProperties;

	public S3CloudStorageProvider(S3Presigner s3Presigner, S3Client s3Client, StorageProperties storageProperties) {
		this.s3Presigner = s3Presigner;
		this.s3Client = s3Client;
		this.storageProperties = storageProperties;
	}

	@Override
	public URL generatePresignedUploadUrl(FileReference fileReference) {
		var builder = AwsRequestOverrideConfiguration.builder();
		if (fileReference.isPublicAccessible()) { // for some reason, SDK doesn't add this automatically at .acl() method
			builder.putRawQueryParameter("x-amz-acl", "public-read");
		}
		PutObjectRequest objReq = PutObjectRequest
						.builder()
						.bucket(getBucket())
						.key(fileReference.getPath())
						.contentType(fileReference.getContentType())
						.contentLength(fileReference.getContentLength())
						.acl(fileReference.isPublicAccessible() ? "public-read" : null)
						.overrideConfiguration(builder.build())
						.build();
		System.out.println(objReq.toString());
		PutObjectPresignRequest presignRequest = PutObjectPresignRequest
						.builder()
						.signatureDuration(Duration.ofMinutes(30))
						.putObjectRequest(objReq)
						.build();
		System.out.println(presignRequest.toString());
		return s3Presigner.presignPutObject(presignRequest).url();
	}

	@Override
	public boolean fileExists(String filePath) {
		if (filePath == null || filePath.isBlank()) {
			return false;
		}
		HeadObjectRequest request = HeadObjectRequest.builder()
						.bucket(getBucket())
						.key(filePath)
						.build();
		try {
			s3Client.headObject(request);
			return true;

		} catch (S3Exception e) {
			if (e.statusCode() == 404) {
				return false;
			}
			throw e;
		}
	}


	private String getBucket() {
		return storageProperties.getS3().getBucketName();
	}
}
