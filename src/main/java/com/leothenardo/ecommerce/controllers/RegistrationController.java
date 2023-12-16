package com.leothenardo.ecommerce.controllers;

import com.leothenardo.ecommerce.dtos.RegisterUserInputDTO;
import com.leothenardo.ecommerce.services.RegistrationService;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/registration")
public class RegistrationController {
	private final RegistrationService registrationService;

	public RegistrationController(RegistrationService registrationService) {
		this.registrationService = registrationService;
	}


	@PostMapping
	public String register(
					@RequestBody RegisterUserInputDTO bodyDTO,
					HttpServletRequest request) {
		System.out.println(appUrl(request));
		return registrationService.register(bodyDTO, appUrl(request));
	}

	@GetMapping(path = "confirm")
	public String confirm(
					@RequestParam("token") String token) {
		return registrationService.confirmToken(token);
	}

	@PostMapping(path = "resend")
	public String resend(
					@RequestParam("token") String token,
					HttpServletRequest request) {
		return registrationService.resendToken(token, appUrlNoPath(request));
	}

	private String appUrl(HttpServletRequest request) {

		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + request.getRequestURI();
	}

	private String appUrlNoPath(HttpServletRequest request) {
		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/api/v1/registration";
	}

}
