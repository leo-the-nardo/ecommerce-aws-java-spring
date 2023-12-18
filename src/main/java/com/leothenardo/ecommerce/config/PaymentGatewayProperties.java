package com.leothenardo.ecommerce.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Validated
@Configuration
@ConfigurationProperties("payment")
public class PaymentGatewayProperties {


	@Valid
	private PaymentGatewayProperties.Asaas asaas = new PaymentGatewayProperties.Asaas();

	public Asaas getAsaas() {
		return asaas;
	}


	public class Asaas {
		@NotBlank
		private String apiKey;
		@NotBlank
		private String apiUrl;

		public String getApiUrl() {
			return apiUrl;
		}

		public void setApiUrl(String apiUrl) {
			this.apiUrl = apiUrl;
		}

		public String getApiKey() {
			return apiKey;
		}

		public void setApiKey(String apiKey) {
			this.apiKey = apiKey;
		}
	}

}
