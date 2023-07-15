package com.seniors.config.security;

import com.seniors.domain.users.entity.Users;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails {

	private Long userId;
	private final String userSnsId;
	private final String userEmail;
	private final String userNickname;
	private final List<GrantedAuthority> authorityList;
	private final String gender;
	private final String profileImageUrl;

	public CustomUserDetails(Long userId, String userSnsId, String userEmail, String userNickname, String gender, String profileImageUrl) {
		this.userId = userId;
		this.userSnsId = userSnsId;
		this.authorityList = new ArrayList<>();
		this.userEmail = userEmail;
		this.userNickname = userNickname;
		this.gender = gender;
		this.profileImageUrl = profileImageUrl;
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

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
