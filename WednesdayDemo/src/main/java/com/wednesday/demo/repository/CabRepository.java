package com.wednesday.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wednesday.demo.model.Cab;

/**
 * @author Jignesh.Rathod
 */
public interface CabRepository extends JpaRepository<Cab, Long> {

}
