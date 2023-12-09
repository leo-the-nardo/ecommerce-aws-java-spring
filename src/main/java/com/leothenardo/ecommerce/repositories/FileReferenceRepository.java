package com.leothenardo.ecommerce.repositories;

import com.leothenardo.ecommerce.models.FileReference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileReferenceRepository extends JpaRepository<FileReference, String> {
}
