package com.leothenardo.ecommerce.repositories;

import com.leothenardo.ecommerce.models.AsaasCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AsaasCustomerRepository extends JpaRepository<AsaasCustomer, String> {
	@Query("SELECT ac FROM AsaasCustomer ac WHERE ac.customerDetails.id = :customerId")
	Optional<AsaasCustomer> findByCustomerDetailsId(@Param("customerId") Long customerId);
}
