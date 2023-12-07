package com.leothenardo.ecommerce.services;

import com.leothenardo.ecommerce.models.Role;
import com.leothenardo.ecommerce.models.User;
import com.leothenardo.ecommerce.projections.UserDetailsProjection;
import com.leothenardo.ecommerce.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
}
