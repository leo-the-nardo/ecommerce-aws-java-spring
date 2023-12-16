package com.leothenardo.ecommerce.dtos;

import java.time.LocalDate;
import java.util.List;

public record RegisterUserInputDTO(
				String name,
				String email,
				String phone,
				String password
) {
}
