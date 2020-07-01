
package com.wednesday.demo.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.wednesday.demo.dto.UserPrincipal;
import com.wednesday.demo.model.Role;
import com.wednesday.demo.model.User;
import com.wednesday.demo.repository.UserRepository;

/**
 * @author Jignesh.Rathod
 */
public class AppUserDetailsServiceDAO implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {

		User usr = userRepo.findByUsername(username);

		if (usr == null) {
			throw new UsernameNotFoundException(username + " not found");
		} else {
			List<SimpleGrantedAuthority> auths = new java.util.ArrayList<SimpleGrantedAuthority>();
			for (Role role : usr.getRoles()) {
				if (role != null) {
					auths.add(new SimpleGrantedAuthority(role.getName()));
				} else {
					auths.add(new SimpleGrantedAuthority("ROLE_USER"));
				}
			}
			UserDetails user = new UserPrincipal(username, usr.getPassword(), usr.getId(), usr.isEnabled(),
					usr.isAccountNonExpired() , usr.isCredentialsNonExpired(), usr.isAccountNonLocked(),
					auths);
			return user;
		}
	}
}
