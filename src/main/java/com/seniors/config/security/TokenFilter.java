package com.seniors.config.security;

import com.seniors.common.exception.type.NotAuthorizedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenFilter extends OncePerRequestFilter {

	private final SecurityService securityService;
	private final TokenService tokenService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
	                                FilterChain filterChain) throws ServletException, IOException {

		try {
			final String token = TokenService.parseTokenByRequest(request);
			if (token != null) {
				CustomUserDetails userDetails = tokenService.getUserDetailsByToken(token);
				securityService.setAuthentication(userDetails);
			}
		} catch (Exception err) {
			throw new NotAuthorizedException("Invalid Token.", err);
		}

		filterChain.doFilter(request, response);
	}

}
