package com.wednesday.demo.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wednesday.demo.dao.UserRepository;
import com.wednesday.demo.model.User;

@RestController
@RequestMapping(value = "${app.base-path}")
public class UserController {

	@Autowired
	UserRepository userRepository;

	private PasswordEncoder passwordEncoder;

	@Autowired
	public UserController(@Lazy PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
	
	@PostMapping("/api/add/admin")
	public ResponseEntity<Object> addAdminDetails(@RequestBody User user) {
		try {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			userRepository.save(user);
			return new ResponseEntity<>(true, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
		}
	}

	@Secured("ROLE_ADMIN")
	@PostMapping("/api/add/user")
	public ResponseEntity<Object> addUserDetails(@RequestBody User user) {
		try {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			userRepository.save(user);
			return new ResponseEntity<>(true, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
		}
	}

}
