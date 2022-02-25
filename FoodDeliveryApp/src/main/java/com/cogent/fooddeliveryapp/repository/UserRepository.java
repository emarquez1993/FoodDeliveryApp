package com.cogent.fooddeliveryapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cogent.fooddeliveryapp.dto.User;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	boolean existsByUserName(String userName);
	boolean existsByEmail(String email);
	Optional<User> findByUserName(String userName);
}
