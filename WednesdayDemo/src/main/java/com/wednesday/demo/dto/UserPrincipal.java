/**
 * Copyright (c) 2018, System Level Solutions (India) Pvt. Ltd.
 * 
 */
package com.wednesday.demo.dto;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


public class UserPrincipal implements UserDetails {


	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private Long userid;

	private List<SimpleGrantedAuthority> authorities;

	private boolean accountNonExpired = true;
	private boolean accountNonLocked = true;
	private boolean credentialsNonExpired = true;
	private boolean enabled = true;

	public UserPrincipal(final String username, final String password, final Long userid, final boolean enabled,
			final boolean accountNonExpired, final boolean credentialsNonExpired, final boolean accountNonLocked,
			final List<SimpleGrantedAuthority> authorities) {
		this.username = username;
		this.password = password;
		this.userid = userid;
		this.enabled = enabled;
		this.accountNonExpired = accountNonExpired;
		this.credentialsNonExpired = credentialsNonExpired;
		this.accountNonLocked = accountNonLocked;
		this.authorities = authorities;
	}

	@Override
	public String getUsername() {
		return username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(final Long userid) {
		this.userid = userid;
	}

	@Override
	public List<SimpleGrantedAuthority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(final List<SimpleGrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(final boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(final boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(final boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}

}
