package com.leothenardo.ecommerce.services;

import com.leothenardo.ecommerce.dtos.RegisterUserInputDTO;
import com.leothenardo.ecommerce.gateways.EmailProvider;
import com.leothenardo.ecommerce.models.ConfirmationToken;
import com.leothenardo.ecommerce.models.CustomerDetails;
import com.leothenardo.ecommerce.models.Role;
import com.leothenardo.ecommerce.models.User;
import com.leothenardo.ecommerce.repositories.UserRepository;
import com.leothenardo.ecommerce.services.exceptions.AlreadyExists;
import com.leothenardo.ecommerce.services.exceptions.ExpiredException;
import com.leothenardo.ecommerce.services.exceptions.ResourceNotFoundException;
import com.leothenardo.ecommerce.validation.EmailValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RegistrationService {

	private final static Long DEFAULT_ROLE_ID = 1L;
	private final UserRepository userRepository;
	private final UserService userService;
	private final EmailValidator emailValidator;
	private final ConfirmationTokenService confirmationTokenService;
	private final EmailProvider emailSender;
	private final PasswordEncoder passwordEncoder;


	public RegistrationService(UserRepository userRepository, UserService userService, EmailValidator emailValidator, ConfirmationTokenService confirmationTokenService, EmailProvider emailSender, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.userService = userService;
		this.emailValidator = emailValidator;
		this.confirmationTokenService = confirmationTokenService;
		this.emailSender = emailSender;
		this.passwordEncoder = passwordEncoder;
	}


	@Transactional
	public String register(RegisterUserInputDTO registerDTO, String appUrl) {
		boolean isValidEmail = emailValidator.
						test(registerDTO.email());

		if (!isValidEmail) {
			throw new IllegalStateException("email not valid");
		}
		User user = new User(
						registerDTO.name(),
						registerDTO.email(),
						registerDTO.phone(),
						registerDTO.cpf(),
						LocalDate.of(2000, 1, 1),
						registerDTO.password()
		);
		String token = this.signUpUser(
						user, appUrl
		);

		return token;
	}

	@Transactional
	public String confirmToken(String token) {
		ConfirmationToken confirmationToken = confirmationTokenService
						.getToken(token)
						.orElseThrow(() ->
										new IllegalStateException("token not found"));

		if (confirmationToken.getConfirmedAt() != null) {
			throw new IllegalStateException("email already confirmed");
		}

		LocalDateTime expiredAt = confirmationToken.getExpiresAt();

		if (expiredAt.isBefore(LocalDateTime.now())) {
			throw new ExpiredException();
		}

		confirmationTokenService.setConfirmedAt(token);
		userService.enableAppUser(
						confirmationToken.getUser().getEmail());
		return "confirmed";
	}

	//'resend' confirmation email if it has expired
	public String resendToken(String token, String appUrl) {
		ConfirmationToken confirmationToken = confirmationTokenService
						.getToken(token)
						.orElseThrow(() ->
										new ResourceNotFoundException(token));

		if (confirmationToken.getConfirmedAt() != null) {
			throw new IllegalStateException("email already confirmed");
		}

		LocalDateTime expiredAt = confirmationToken.getExpiresAt();

		//TODO: make it not an service responsibility, abstraction leak
		if (expiredAt.isBefore(LocalDateTime.now())) {
			String newToken = UUID.randomUUID().toString();
			confirmationToken.setToken(newToken);
			confirmationToken.setCreatedAt(LocalDateTime.now());
			confirmationToken.setExpiresAt(LocalDateTime.now().plusMinutes(15));
			confirmationTokenService.saveConfirmationToken(confirmationToken);
			User newUser = confirmationToken.getUser();
			String link = appUrl + "/confirm?token=" + newToken;
			String to = newUser.getEmail();
			emailSender.sendConfirmationEmail(to, link, newUser.getName());
			return "Sent new confirmation email";
		}

		return "Not expired";
	}


	@Transactional
	public String signUpUser(User newUser, String appUrl) {
		boolean userExists = userRepository
						.findByEmail(newUser.getEmail())
						.isPresent();

		if (userExists) {
			// TODO check of attributes are the same and
			// TODO if email not confirmed send confirmation email.

			throw new AlreadyExists(newUser.getEmail());
		}

		String encodedPassword = passwordEncoder.encode(newUser.getPassword());
		newUser.setPassword(encodedPassword);
		Role defaultRole = new Role(DEFAULT_ROLE_ID, "ROLE_CLIENT"); //TODO: think about this
		newUser.getRoles().add(defaultRole);
		CustomerDetails customerDetails = new CustomerDetails();
		customerDetails.setUser(newUser);
		newUser.setCustomerDetails(customerDetails);

		String token = UUID.randomUUID().toString();
		String link = appUrl + "/confirm?token=" + token;
		emailSender.sendConfirmationEmail(newUser.getEmail(), link, newUser.getName());

		userRepository.save(newUser);

		ConfirmationToken confirmationToken = new ConfirmationToken(
						token,
						LocalDateTime.now(),
						LocalDateTime.now().plusMinutes(15),
						newUser
		);

		confirmationTokenService.saveConfirmationToken(confirmationToken);
		return token;
	}


}
