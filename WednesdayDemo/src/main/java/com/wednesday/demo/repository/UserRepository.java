package com.wednesday.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wednesday.demo.model.User;

/**
 * @author Jignesh.Rathod
 */
public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsername(String username);
	
}
