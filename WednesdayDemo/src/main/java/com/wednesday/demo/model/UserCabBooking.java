package com.wednesday.demo.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "usercabbooking")
public class UserCabBooking {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bookingid;

	private Double sourceLatitude;
	private Double sourceLongitude;
	private Double destLatitude;
	private Double destLongitude;

	private Double distance;
	private Double fare;

	@ManyToOne
	private User user;
	
	public Long getBookingid() {
		return bookingid;
	}

	public void setBookingid(Long bookingid) {
		this.bookingid = bookingid;
	}

	public Double getSourceLatitude() {
		return sourceLatitude;
	}

	public void setSourceLatitude(Double sourceLatitude) {
		this.sourceLatitude = sourceLatitude;
	}

	public Double getSourceLongitude() {
		return sourceLongitude;
	}

	public void setSourceLongitude(Double sourceLongitude) {
		this.sourceLongitude = sourceLongitude;
	}

	public Double getDestLatitude() {
		return destLatitude;
	}

	public void setDestLatitude(Double destLatitude) {
		this.destLatitude = destLatitude;
	}

	public Double getDestLongitude() {
		return destLongitude;
	}

	public void setDestLongitude(Double destLongitude) {
		this.destLongitude = destLongitude;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public Double getFare() {
		return fare;
	}

	public void setFare(Double fare) {
		this.fare = fare;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
