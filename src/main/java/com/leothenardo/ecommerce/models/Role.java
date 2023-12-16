package com.leothenardo.ecommerce.models;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;

@Entity
@Table(name = "tb_role")
public class Role implements GrantedAuthority {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String authority;

	public Role() {
	}

	public Role(Long id, String authority) {
		this.id = id;
		this.authority = authority;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Role role = (Role) o;
		return Objects.equals(authority, role.authority);
	}

	@Override
	public int hashCode() {
		return Objects.hash(authority);
	}

	public Long getId() {
		return id;
	}

	@Override
	public String getAuthority() {
		return authority;
	}

	//"USER" , "ADMIN" authority static factory methods


}
