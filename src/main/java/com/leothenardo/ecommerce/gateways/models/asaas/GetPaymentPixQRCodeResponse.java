package com.leothenardo.ecommerce.gateways.models.asaas;

import java.time.LocalDateTime;

public class GetPaymentPixQRCodeResponse {

	private String encodedImage;
	private String payload;
	private String expirationDate;

	public GetPaymentPixQRCodeResponse() {
	}

	public GetPaymentPixQRCodeResponse(String encodedImage, String payload, String expirationDate) {
		this.encodedImage = encodedImage;
		this.payload = payload;
		this.expirationDate = expirationDate;
	}

	public String getEncodedImage() {
		return encodedImage;
	}

	public void setEncodedImage(String encodedImage) {
		this.encodedImage = encodedImage;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
}
