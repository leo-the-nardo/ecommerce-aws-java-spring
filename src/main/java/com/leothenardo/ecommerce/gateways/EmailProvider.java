package com.leothenardo.ecommerce.gateways;

public interface EmailProvider {
	void send(String to, String as, String title, String content);


}
