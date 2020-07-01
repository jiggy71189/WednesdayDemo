package com.wednesday.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wednesday.demo.model.UserCabBooking;

/**
 * @author Jignesh.Rathod
 */
public interface UserCabBookingRepository extends JpaRepository<UserCabBooking, Long> {

}
