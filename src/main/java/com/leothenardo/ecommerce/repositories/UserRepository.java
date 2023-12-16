package com.leothenardo.ecommerce.repositories;


import com.leothenardo.ecommerce.models.User;
import com.leothenardo.ecommerce.projections.UserDetailsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	@Query(nativeQuery = true, value = """
					 			SELECT tb_user.email AS username,
					 			 tb_user.password,
					 			  tb_role.id AS roleId,
					 			   tb_role.authority,
					 			    tb_user.is_confirmed AS isEnabled
						FROM tb_user
						INNER JOIN tb_user_role ON tb_user.id = tb_user_role.user_id
						INNER JOIN tb_role ON tb_role.id = tb_user_role.role_id
						WHERE tb_user.email = :email
					""")
	List<UserDetailsProjection> searchUserAndRole(String email);

	@Transactional
	@Modifying
	@Query("UPDATE User a " +
					"SET a.isConfirmed = TRUE WHERE a.email = ?1")
	int enableAppUser(String email);

}
