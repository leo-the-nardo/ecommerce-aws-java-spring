package com.leothenardo.ecommerce.repositories;

import com.leothenardo.ecommerce.models.AsaasCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AsaasCustomerRepository extends JpaRepository<AsaasCustomer, String> {
	Optional<AsaasCustomer> findByUserId(Long id);
}
