package com.wednesday.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wednesday.demo.model.UserCabBooking;

public interface UserCabBookingRepository extends JpaRepository<UserCabBooking, Long> {

}
