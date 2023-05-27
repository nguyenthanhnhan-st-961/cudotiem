package com.cudotiem.authservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cudotiem.authservice.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	@Query("SELECT u FROM User u WHERE u.username = ?1 or u.email = ?1")
	Optional<User> findByUsernameOrEmail(String usernameOrEmail);
	@Query("SELECT u FROM User u WHERE u.verificationCode = ?1")
    public User findByVerificationCode(String code);
	Boolean existsByEmail(String email);
	Boolean existsByUsername(String username);
}
