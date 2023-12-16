package com.leothenardo.ecommerce.services;

import com.leothenardo.ecommerce.gateways.EmailProvider;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;

import java.io.UnsupportedEncodingException;


@Service
public class EmailService implements EmailProvider {
	private final static Logger log = LoggerFactory
					.getLogger(EmailService.class);
	private final JavaMailSender mailSender;

	@Value("${spring.mail.username}")
	private String from;

	public EmailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void sendOrderConfirmationEmail(Long orderId) {
		System.out.println("Sending email to order " + orderId);
	}

	@Override
	@Async
	public void send(String to, String email) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper =
							new MimeMessageHelper(mimeMessage, "utf-8");
			helper.setText(email, true);
			helper.setTo(to);
			helper.setSubject("Confirm your email");
			helper.setFrom(from, "User Registration Portal Service");
			mailSender.send(mimeMessage);

		} catch (MessagingException e) {
			log.error("failed to send email", e);
			throw new IllegalStateException("failed to send email");

		} catch (UnsupportedEncodingException e) {
			log.error("failed to send email", e);
			throw new RuntimeException(e);
		}
	}
}
