package com.leothenardo.ecommerce.projections;

public interface UserDetailsProjection {
	String getUsername();

	String getPassword();

	Long getRoleId();

	String getAuthority();
}

