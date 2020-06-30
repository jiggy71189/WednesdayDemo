package com.wednesday.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wednesday.demo.model.Cab;

public interface CabRepository extends JpaRepository<Cab, Long> {

}
