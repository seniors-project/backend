package com.seniors.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seniors.common.constant.ResultCode;
import com.seniors.common.dto.ErrorResponse;
import com.seniors.common.exception.type.NotAuthorizedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
	                                FilterChain filterChain) throws IOException {

		try {
			filterChain.doFilter(request, response);
		} catch (NotAuthorizedException e) {
			log.error("Not Authorized.", e);
			ErrorResponse errorResponse = ErrorResponse.of(ResultCode.UNAUTHORIZED, e.getMessage());
			response.setStatus(HttpStatus.UNAUTHORIZED.value());

			setHeader(response);

			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(errorResponse);
			response.getWriter().write(json);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (Exception e) {
			log.error("Internal Server Error.", e);
			ErrorResponse errorResponse = ErrorResponse.of(ResultCode.UNAUTHORIZED, e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

			setHeader(response);

			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(errorResponse);
			response.getWriter().write(json);
			response.getWriter().flush();
			response.getWriter().close();
		}
	}

	private void setHeader(HttpServletResponse response) {
		response.setHeader("content-type", "application/json;charset=UTF-8");
		response.setHeader("cache-control", "no-cache, no-store, max-age=0, must-revalidate");
		response.setHeader("expires", "0");
		response.setHeader("pragma", "no-cache'");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
	}
}
