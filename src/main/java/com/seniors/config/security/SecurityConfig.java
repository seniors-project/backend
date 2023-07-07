package com.seniors.config.security;


import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.reactive.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsUtils;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

//	private final TokenFilter tokenFilter;
//	private final ExceptionHandlerFilter exceptionHandlerFilter;

	private static final String[] permitAllUrl = {
			"/",
			"/index.html",
			"/favicon.ico",
			"/robots.txt",
			"/post/**",
			"/api/post/**",
			"/assets/**", // static 경로 추가
			"/public/**",
			"/public/fonts/**",
			"/fonts/**",
			"/css/**",
			"/images/**",
			"/js/**",
			"/enums/**",
			"/join/verification-url",
			"/view/users/change-password",
	};

	@Bean
	public WebSecurityCustomizer configure() {
		return (web) -> web.ignoring().requestMatchers(String.valueOf(PathRequest.toStaticResources().atCommonLocations()))
				.requestMatchers(
						permitAllUrl
				);
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		log.warn("accessDeniedHandler");
		return (request, response, e) -> {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.setContentType("text/plain;charset=UTF-8");
			response.getWriter().write("ACCESS DENIED");
			response.getWriter().flush();
			response.getWriter().close();
		};
	}

	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return (request, response, e) -> {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("text/plain;charset=UTF-8");
			response.getWriter().write("UNAUTHORIZED");
			response.getWriter().flush();
			response.getWriter().close();
		};
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

		httpSecurity.csrf().disable().cors().disable()
				.authorizeHttpRequests(req -> req
						.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
						.requestMatchers(permitAllUrl).permitAll()
						.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
						.anyRequest().permitAll()
				)
				.formLogin()
				.disable()
				.headers()
				.frameOptions()
				.sameOrigin()
				.and()
				.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/api/auth/logout"))
				.logoutSuccessUrl("/")
				.invalidateHttpSession(true)
				.and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.exceptionHandling();
//		        .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class)
//				.addFilterBefore(exceptionHandlerFilter, TokenFilter.class);
		return httpSecurity.build();
	}
}