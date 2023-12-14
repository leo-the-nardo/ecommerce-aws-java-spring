package com.leothenardo.ecommerce.services;

import com.leothenardo.ecommerce.dtos.UserDTO;
import com.leothenardo.ecommerce.models.Role;
import com.leothenardo.ecommerce.models.User;
import com.leothenardo.ecommerce.projections.UserDetailsProjection;
import com.leothenardo.ecommerce.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<UserDetailsProjection> queryResult = userRepository.searchUserAndRole(username);
		if (queryResult.isEmpty()) {
			throw new UsernameNotFoundException("Email not found");
		}
		UserDetailsProjection userProjection = queryResult.get(0);
		User userJpaEntity = new User();
		userJpaEntity.setEmail(userProjection.getUsername());
		userJpaEntity.setPassword(userProjection.getPassword());

		queryResult.forEach(userRow -> userJpaEntity.addRole(
						new Role(userRow.getRoleId(), userRow.getAuthority())
		));
		return userJpaEntity;
	}

	protected User authenticated() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Jwt jwtPrincipal = (Jwt) authentication.getPrincipal();
		String emailJwt = jwtPrincipal.getClaim("username");
		return userRepository.findByEmail(emailJwt).orElseThrow(() -> new UsernameNotFoundException("Email not found"));
	}

	@Transactional(readOnly = true) // readOnly = true -> Don't need to lock the database
	public UserDTO getMe() {
		User user = authenticated();
		return UserDTO.from(user);
	}
}
