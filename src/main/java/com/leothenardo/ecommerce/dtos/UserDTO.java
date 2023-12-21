package com.leothenardo.ecommerce.dtos;

import com.leothenardo.ecommerce.models.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record UserDTO(
				Long id,
				String name,
				String email,
				String cpf,
				String phone,
				LocalDate birthDate,
				List<String> roles) {

	public static UserDTO from(User user) {
		List<String> roles = new ArrayList<>();
		user.getRoles().forEach(role -> roles.add(role.getAuthority()));
		System.out.println("UserDTO returned-------");
		return new UserDTO(
						user.getId(),
						user.getName(),
						user.getEmail(),
						user.getCpf(),
						user.getPhone(),
						user.getBirthDate(),
						roles);
	}
}
