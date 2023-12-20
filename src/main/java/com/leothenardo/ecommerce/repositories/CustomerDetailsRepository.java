package com.leothenardo.ecommerce.repositories;

import com.leothenardo.ecommerce.models.CustomerDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerDetailsRepository extends JpaRepository<CustomerDetails, Long> {
}
