package com.leothenardo.ecommerce.dtos;

import java.time.LocalDateTime;

public record PaymentPixDetailsResponseDTO(
				String encodedQRCodeImage,
				String copiaECola,
				String expirationDate
) {

}
