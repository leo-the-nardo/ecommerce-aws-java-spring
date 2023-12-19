package com.leothenardo.ecommerce.services;

import com.leothenardo.ecommerce.dtos.RegisterUserInputDTO;
import com.leothenardo.ecommerce.gateways.EmailProvider;
import com.leothenardo.ecommerce.models.ConfirmationToken;
import com.leothenardo.ecommerce.models.Role;
import com.leothenardo.ecommerce.models.User;
import com.leothenardo.ecommerce.repositories.UserRepository;
import com.leothenardo.ecommerce.services.exceptions.AlreadyExists;
import com.leothenardo.ecommerce.services.exceptions.ExpiredException;
import com.leothenardo.ecommerce.services.exceptions.ResourceNotFoundException;
import com.leothenardo.ecommerce.validation.EmailValidator;
import jakarta.servlet.http.HttpServletRequest;
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


	public String register(RegisterUserInputDTO registerDTO, String appUrl) {
		boolean isValidEmail = emailValidator.
						test(registerDTO.email());

		if (!isValidEmail) {
			throw new IllegalStateException("email not valid");
		}

		String token = this.signUpUser(
						new User(
										registerDTO.name(),
										registerDTO.email(),
										registerDTO.phone(),
										registerDTO.cpf(),
										LocalDate.of(2000, 1, 1),
										registerDTO.password()
						), appUrl
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
						confirmationToken.getAppUser().getEmail());
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

			String link = appUrl + "/confirm?token=" + newToken;
			String to = confirmationToken.getAppUser().getEmail();
			String content = buildEmail(confirmationToken.getAppUser().getName(), link);
			emailSender.send(to,
							"Registration Portal Service",
							"Confirm your email",
							content);
			return "Sent new confirmation email";
		}

		return "Not expired";
	}


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

		String token = UUID.randomUUID().toString();
		String link = appUrl + "/confirm?token=" + token;
		String content = buildEmail(newUser.getName(), link);
		emailSender.send(
						newUser.getEmail(),
						"Registration Portal Service",
						"Confirm your email",
						content);  //TODO: be resilient to email errors

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


	private String buildEmail(String name, String link) {
		return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
						"\n" +
						"<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
						"\n" +
						"  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
						"    <tbody><tr>\n" +
						"      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
						"        \n" +
						"        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
						"          <tbody><tr>\n" +
						"            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
						"                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
						"                  <tbody><tr>\n" +
						"                    <td style=\"padding-left:10px\">\n" +
						"                  \n" +
						"                    </td>\n" +
						"                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
						"                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
						"                    </td>\n" +
						"                  </tr>\n" +
						"                </tbody></table>\n" +
						"              </a>\n" +
						"            </td>\n" +
						"          </tr>\n" +
						"        </tbody></table>\n" +
						"        \n" +
						"      </td>\n" +
						"    </tr>\n" +
						"  </tbody></table>\n" +
						"  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
						"    <tbody><tr>\n" +
						"      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
						"      <td>\n" +
						"        \n" +
						"                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
						"                  <tbody><tr>\n" +
						"                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
						"                  </tr>\n" +
						"                </tbody></table>\n" +
						"        \n" +
						"      </td>\n" +
						"      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
						"    </tr>\n" +
						"  </tbody></table>\n" +
						"\n" +
						"\n" +
						"\n" +
						"  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
						"    <tbody><tr>\n" +
						"      <td height=\"30\"><br></td>\n" +
						"    </tr>\n" +
						"    <tr>\n" +
						"      <td width=\"10\" valign=\"middle\"><br></td>\n" +
						"      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
						"        \n" +
						"            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
						"        \n" +
						"      </td>\n" +
						"      <td width=\"10\" valign=\"middle\"><br></td>\n" +
						"    </tr>\n" +
						"    <tr>\n" +
						"      <td height=\"30\"><br></td>\n" +
						"    </tr>\n" +
						"  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
						"\n" +
						"</div></div>";
	}


}
