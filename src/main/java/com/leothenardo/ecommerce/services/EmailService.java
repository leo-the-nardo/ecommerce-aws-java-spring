package com.leothenardo.ecommerce.services;

import com.leothenardo.ecommerce.dtos.mail.ConfirmationOrderMailInput;
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
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;


@Service
public class EmailService implements EmailProvider {
	private final static Logger log = LoggerFactory
					.getLogger(EmailService.class);
	private final JavaMailSender mailSender;
	private final TemplateEngine templateEngine;

	@Value("${spring.mail.username}")
	private String from;

	public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
		this.mailSender = mailSender;
		this.templateEngine = templateEngine;
	}

	@Override
	@Async
	public void send(String to, String as, String title, String content) {
		try {
			Context context = new Context();
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper =
							new MimeMessageHelper(mimeMessage, "utf-8");
			helper.setText(content, true);
			helper.setTo(to);
			helper.setSubject(title);
			helper.setFrom(from, as);
			mailSender.send(mimeMessage);

		} catch (MessagingException e) {
			log.error("failed to send email", e);
			throw new IllegalStateException("failed to send email");

		} catch (UnsupportedEncodingException e) {
			log.error("failed to send email", e);
			throw new RuntimeException(e);
		}
	}

	@Async
	public void sendConfirmationEmail(String to, String confirmationUrl, String name) {
		Context context = new Context();
		context.setVariable("confirmationUrl", confirmationUrl);
		context.setVariable("name", name);
		String content = templateEngine.process("email/emailconfirmation", context);
		send(to, "Leothenardo", "Email Confirmation", content);

	}


	@Async
	public void sendOrderConfirmationEmail(ConfirmationOrderMailInput input) {
		Context context = new Context();
		context.setVariable("name", input.getName());
		context.setVariable("items", input.getItems());
		context.setVariable("total", input.getTotal());
		String content = templateEngine.process("email/orderconfirmation", context);

		send(input.getTo(), "Leothenardo", "Order Confirmation", content);
	}

}
