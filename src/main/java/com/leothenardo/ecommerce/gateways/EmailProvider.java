package com.leothenardo.ecommerce.gateways;

import com.leothenardo.ecommerce.dtos.mail.ConfirmationOrderMailInput;

public interface EmailProvider {
	void send(String to, String as, String title, String content);

	void sendConfirmationEmail(String to, String confirmationUrl, String name);

	void sendOrderConfirmationEmail(ConfirmationOrderMailInput input);

}
