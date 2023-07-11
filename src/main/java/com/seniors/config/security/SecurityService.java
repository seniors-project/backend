package com.seniors.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityService {

	public void setAuthentication(CustomUserDetails userDetails) {
		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken(
						userDetails
						, null
						, userDetails.getAuthorities()));
	}

	public CustomUserDetails getAuthentication() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null
				|| !authentication.isAuthenticated()
				|| authentication.getName().equals("anonymousUser")) {
			return null;
		}

		return (CustomUserDetails) authentication.getPrincipal();
	}

	public void signOut() {
		SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
	}

}
