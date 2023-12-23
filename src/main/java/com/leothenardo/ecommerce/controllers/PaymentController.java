package com.leothenardo.ecommerce.controllers;

import com.leothenardo.ecommerce.dtos.PaymentUrlRequestDTO;
import com.leothenardo.ecommerce.dtos.PaymentUrlResponseDTO;
import com.leothenardo.ecommerce.dtos.sensitive.ProcessCheckoutSecureDTO;
import com.leothenardo.ecommerce.dtos.sensitive.TokenizeCardRequestSecureDTO;
import com.leothenardo.ecommerce.dtos.webhooks.AsaasPayWebhook;
import com.leothenardo.ecommerce.services.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

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
					@RequestBody AsaasPayWebhook bodyDTO
	) {
		paymentService.handleAsaasWebhook(bodyDTO);
		return ResponseEntity.ok().build();
	}

	@PreAuthorize("hasAnyRole('ROLE_CLIENT','ROLE_ADMIN')")
	@PostMapping(value = "/asaas/tokenize")
	public ResponseEntity<Void> tokenizeCard(
					@RequestBody TokenizeCardRequestSecureDTO bodyDTO
	) {
		paymentService.tokenizeCard(bodyDTO);
		return ResponseEntity.ok().build();
	}

	@PreAuthorize("hasAnyRole('ROLE_CLIENT','ROLE_ADMIN')")
	@PostMapping(value = "/asaas/process")
	public ResponseEntity<Void> processCheckout(
					@RequestBody ProcessCheckoutSecureDTO bodyDTO
	) {
		boolean success = paymentService.processCheckout(bodyDTO);
		if (!success) {
			return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).build();
		}
		return ResponseEntity.ok().build();
	}

	@PreAuthorize("hasAnyRole('ROLE_CLIENT','ROLE_ADMIN')")
	@GetMapping(value = "/asaas/pix/{orderId}/sse")
	SseEmitter sse(@PathVariable("orderId") String orderId) {
		return paymentService.ssePix(orderId);
	}

}
