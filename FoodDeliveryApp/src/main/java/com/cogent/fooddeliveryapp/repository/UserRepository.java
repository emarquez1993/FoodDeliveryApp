package com.cogent.fooddeliveryapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cogent.fooddeliveryapp.dto.User;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
