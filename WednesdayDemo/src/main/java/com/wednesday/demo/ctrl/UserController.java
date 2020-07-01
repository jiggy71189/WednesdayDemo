package com.wednesday.demo.ctrl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wednesday.demo.dto.TransactionInfo;
import com.wednesday.demo.dto.UserPrincipal;
import com.wednesday.demo.model.Role;
import com.wednesday.demo.model.User;
import com.wednesday.demo.model.UserCabBooking;
import com.wednesday.demo.repository.UserCabBookingRepository;
import com.wednesday.demo.repository.UserRepository;

/**
 * @author Jignesh.Rathod
 */
@RestController
@RequestMapping(value = "${app.base-path}" + "/user")
public class UserController {

	@Autowired
	UserRepository userRepository;

	private PasswordEncoder passwordEncoder;

	@Autowired
	UserCabBookingRepository UserCabBookingRepo;

	@Autowired
	public UserController(@Lazy PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Secured("ROLE_ADMIN")
	@PostMapping("/add")
	public ResponseEntity<Object> addUserDetails(@RequestBody User user) {
		try {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			Role role = new Role();
			role.setId(2L);
			Set<Role> set = new HashSet<Role>();
			set.add(role);
			user.setRoles(set);
			userRepository.save(user);
			return new ResponseEntity<>(new TransactionInfo(true), HttpStatus.CREATED);
		} catch (Exception e) {
			TransactionInfo transInfo = new TransactionInfo(false);
			transInfo.addErrorList(e.getCause().getMessage());
			return new ResponseEntity<>(transInfo, HttpStatus.BAD_REQUEST);
		}
	}

	@Secured("ROLE_USER")
	@GetMapping("/book/history")
	private ResponseEntity<Object> getUserBookHistrory() {

		try {
			UserPrincipal usrPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();

			Long userid = usrPrincipal.getUserid();

			List<UserCabBooking> userHistory = UserCabBookingRepo.findAll().stream()
					.filter(r -> r.getUser().getId() == userid).collect(Collectors.toList());

			return new ResponseEntity<>(new TransactionInfo(userHistory), HttpStatus.OK);
		} catch (Exception e) {
			TransactionInfo transInfo = new TransactionInfo(false);
			transInfo.addErrorList(e.getCause().getMessage());
			return new ResponseEntity<>(transInfo, HttpStatus.BAD_REQUEST);
		}
	}

}
