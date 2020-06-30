package com.wednesday.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wednesday.demo.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsername(String username);
	
}
