package com.leothenardo.ecommerce.controllers;

import com.leothenardo.ecommerce.dtos.PaymentUrlRequestDTO;
import com.leothenardo.ecommerce.dtos.PaymentUrlResponseDTO;
import com.leothenardo.ecommerce.dtos.webhooks.AsaasPayWebhook;
import com.leothenardo.ecommerce.services.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping(value = "/payments")
public class PaymentController {
	private final PaymentService paymentService;

	public PaymentController(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	@PreAuthorize("hasAnyRole('ROLE_CLIENT')")
	@PostMapping(value = "/asaas/url")
	public ResponseEntity<PaymentUrlResponseDTO> generatePaymentUrl(
					@RequestBody PaymentUrlRequestDTO bodyDTO
	) {
		String url = paymentService.generateUrl(bodyDTO);
		URI uri = URI.create(url);
		return ResponseEntity.created(uri).body(new PaymentUrlResponseDTO(url));
	}

	@PostMapping(value = "/asaas/webhook")
	public ResponseEntity<Void> asaasWebhook(
					@RequestBody AsaasPayWebhook body
	) {
		paymentService.handleAsaasWebhook(body);
		return ResponseEntity.ok().build();
	}
}
