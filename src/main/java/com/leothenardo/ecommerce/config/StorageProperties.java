package com.leothenardo.ecommerce.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;


import java.net.URL;

@Validated
@Configuration
@ConfigurationProperties("aw.storage")
public class StorageProperties {

	@Valid
	private S3 s3 = new S3();

	@Valid
	private Image image = new Image();

	public S3 getS3() {
		return s3;
	}
	
	public Image getImage() {
		return image;
	}

	public class S3 {
		@NotBlank
		private String accessKey;
		@NotBlank
		private String secretKey;
		@NotBlank
		private String bucketName;
		@NotBlank
		private String region;

		public String getAccessKey() {
			return accessKey;
		}

		public void setAccessKey(String accessKey) {
			this.accessKey = accessKey;
		}

		public String getSecretKey() {
			return secretKey;
		}

		public void setSecretKey(String secretKey) {
			this.secretKey = secretKey;
		}

		public String getBucketName() {
			return bucketName;
		}

		public void setBucketName(String bucketName) {
			this.bucketName = bucketName;
		}

		public String getRegion() {
			return region;
		}

		public void setRegion(String region) {
			this.region = region;
		}
	}

	public class Image {
		@NotNull
		private URL downloadUrl;

		public URL getDownloadUrl() {
			return downloadUrl;
		}

		public void setDownloadUrl(URL downloadUrl) {
			this.downloadUrl = downloadUrl;
		}

	}

}
