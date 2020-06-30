package com.wednesday.demo.ctrl;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wednesday.demo.dao.CabRepository;
import com.wednesday.demo.dao.UserCabBookingRepository;
import com.wednesday.demo.dto.UserPrincipal;
import com.wednesday.demo.model.Cab;
import com.wednesday.demo.model.User;
import com.wednesday.demo.model.UserCabBooking;
import com.wednesday.demo.utils.GeneralUtils;

@RestController
@RequestMapping(value = "${app.base-path}")
public class CabController {

	@Autowired
	UserCabBookingRepository UserCabBookingRepo;

	@Autowired
	CabRepository cabRepository;

	@Secured("ROLE_ADMIN")
	@PostMapping("/api/add/cab")
	private ResponseEntity<Object> addCabDetail(@Valid @RequestBody Cab cabDetail) {

		cabRepository.save(cabDetail);

		return new ResponseEntity<>(true, HttpStatus.OK);
	}

	@Secured("ROLE_USER")
	@GetMapping("/api/cab/{latitude}/{longitude}")
	private ResponseEntity<Object> getNearbyCablist(@PathVariable Double latitude, @PathVariable Double longitude) {

		List<Cab> list = cabRepository.findAll();
		List<Cab> nearby = new ArrayList<>();
		Double dist = null;
		for (int i = 0; i < list.size(); i++) {
			dist = GeneralUtils.distance(latitude, longitude, list.get(i).getCurrentLatitude(),
					list.get(i).getCurrentLongitude(), "k");
			if (dist < 2.0d) {
				nearby.add(list.get(i));
			}
		}

		return new ResponseEntity<>(nearby, HttpStatus.OK);
	}

	@Secured("ROLE_USER")
	@GetMapping("/api/user/history")
	private ResponseEntity<Object> getUserBookHistrory(@PathVariable Double latitude, @PathVariable Double longitude) {

		UserPrincipal usrPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		Long userid = usrPrincipal.getUserid();

		List<UserCabBooking> list = UserCabBookingRepo.findAll();
		List<UserCabBooking> userHistory = new ArrayList<>();
		Double dist = null;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getUser().getId() == userid) {
				userHistory.add(list.get(i));
			}
		}

		return new ResponseEntity<>(userHistory, HttpStatus.OK);
	}

	@Secured("ROLE_USER")
	@PostMapping("/api/add/booking")
	private ResponseEntity<Object> addCabBooking(@Valid @RequestBody UserCabBooking bookingDetails) {

		UserPrincipal usrPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		Long userid = usrPrincipal.getUserid();
		bookingDetails.setUser(new User(userid));

		Double distance = GeneralUtils.distance(bookingDetails.getSourceLatitude(), bookingDetails.getSourceLongitude(),
				bookingDetails.getDestLatitude(), bookingDetails.getDestLongitude(), "k");

		bookingDetails.setDistance(distance);
		UserCabBooking obj = UserCabBookingRepo.save(bookingDetails);

		return new ResponseEntity<>(obj, HttpStatus.OK);
	}

}
