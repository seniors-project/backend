package com.seniors.config.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails {

	private final Long userId;
	private final String userEmail;
	private final String userNickname;
	private final String userPhoneNumber;
	private final List<GrantedAuthority> authorityList;

	public CustomUserDetails(Long userId, String userEmail, String userNickname,
	                         String userPhoneNumber) {
		this.userId = userId;
		this.authorityList = new ArrayList<>();
		this.userEmail = userEmail;
		this.userNickname = userNickname;
		this.userPhoneNumber = userPhoneNumber;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorityList;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return userId.toString();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
