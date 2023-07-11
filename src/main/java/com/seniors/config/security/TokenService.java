package com.seniors.config.security;

import com.seniors.common.constant.ResultCode;
import com.seniors.common.exception.type.NotAuthorizedException;
import com.seniors.common.exception.type.SignInException;
import com.seniors.domain.users.entity.Users;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

	private static Long defaultExpirationMinutes;

	@Value("${jwt.secret-key}")
	private static String secretKey;

	@Value("${util.jwt.defaultExpirationMinutes}")
	public void setDefaultExpirationMinutes(Long defaultExpirationMinutes) {
		TokenService.defaultExpirationMinutes = defaultExpirationMinutes;
	}

	public static String parseTokenByRequest(HttpServletRequest request) {
		final String authHeader = request.getHeader("authorization");
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			return authHeader.substring(7);
		}
		return null;
	}

	public static String parseReFreshTokenByRequest(HttpServletRequest request) {
		final String authHeader = request.getHeader("refresh-token");
		return authHeader;
	}

	public static Map<String, Object> generateDefaultClaims(Users user, Long plusExpMinutes) {
		LocalDateTime now = LocalDateTime.now();

		Map<String, Object> claims = new HashMap<>();
		claims.put("sub", "access-token");
		claims.put("iss", secretKey);
		claims.put("iat", Timestamp.valueOf(now));
		claims.put("exp", Timestamp.valueOf(now.plusMinutes(plusExpMinutes)).getTime() / 1000);
		claims.put("userId", user.getId());
		claims.put("userNickname", user.getNickname());
		claims.put("userEmail", user.getEmail());
		claims.put("created",
				LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));

		return claims;
	}

	public static Map<String, Object> generateReFreshClaims(Users user, Long plusExpMinutes) {
		LocalDateTime now = LocalDateTime.now();

		Map<String, Object> claims = new HashMap<>();
		claims.put("sub", "refresh-token");
		claims.put("iss", secretKey);
		claims.put("userId", user.getId());
		claims.put("iat", Timestamp.valueOf(now));
		claims.put("exp", Timestamp.valueOf(now.plusMinutes(plusExpMinutes)).getTime() / 1000);
		claims.put("created",
				LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));

		return claims;
	}

	public static String generateToken(Users user, Boolean remember) {
		Long expMin = remember ? defaultExpirationMinutes * 24 * 7 : defaultExpirationMinutes;
		Map<String, Object> claims = generateDefaultClaims(user, expMin);

		return JwtUtil.generateToken(claims);
	}

	public static String generateRefreshToken(Users user, Boolean remember) {
		Long expMin = remember ? defaultExpirationMinutes * 24 * 30 : defaultExpirationMinutes * 4;
		Map<String, Object> claims = generateReFreshClaims(user, expMin);

		return JwtUtil.generateRefreshToken(claims);
	}

	public static String generateToken(Users user, Long plusExpMinutes) {
		Map<String, Object> claims = generateDefaultClaims(user, plusExpMinutes);

		return JwtUtil.generateToken(claims);
	}

	public static String generateRefreshToken(Users user, Long plusExpMinutes) {
		Map<String, Object> claims = generateReFreshClaims(user, plusExpMinutes);

		return JwtUtil.generateRefreshToken(claims);
	}


	public static CustomUserDetails getUserDetailsByToken(String token) {
		Map<String, Object> claims = JwtUtil.getClaims(token);
		if (claims == null) {
			throw new NotAuthorizedException("Invalid token (" + token + ")");
		}

		// token정보 이외에 추가로 정보가 필요해서 DB의 데이터를 조회해서 userDetails정보를 만드려면 UserDetailsService 인터페이스를 캐시서비스 또는 유저서비스에서 구현해서 userDetails를 리턴하게 하면 된다.
		// 꼭 UserDetailsService 인터페이스를 구현하지 않아도 되고 DB를 조회해서 userDetails에 데이터를 넣어서 리턴해주게 만들면된다.
		Long userId = Long.parseLong(claims.get("userId").toString());
		String userEmail = claims.get("userEmail").toString();
		String userNickname = claims.get("userNickname").toString();
		String userPhoneNumber = claims.get("userPhoneNumber").toString();

		return new CustomUserDetails(userId, userEmail, userNickname, userPhoneNumber);
	}

	public static CustomUserDetails getUserDetailsByRefreshToken(String token)
			throws SignInException {
		Map<String, Object> claims = null;
		try {
			claims = JwtUtil.getClaimsForReFresh(token);
		} catch (Exception e) {
			throw new SignInException(ResultCode.EXPIRED_REFRESH_TOKEN,
					"Invalid token (" + token + ")");
		}

		if (claims == null) {
			throw new SignInException(ResultCode.EXPIRED_REFRESH_TOKEN,
					"Invalid token (" + token + ")");
		}

		Long userId = Long.parseLong(claims.get("userId").toString());

		return new CustomUserDetails(userId, null, null,  null);
	}

	public static String generateMailVerificationToken(Users user, Long plusExpMinutes) {
		LocalDateTime now = LocalDateTime.now();

		Map<String, Object> claims = new HashMap<>();
		claims.put("sub", "mail-verification");
		claims.put("iss", secretKey);
		claims.put("iat", Timestamp.valueOf(now));
		claims.put("exp", Timestamp.valueOf(now.plusMinutes(plusExpMinutes)).getTime() / 1000);
		claims.put("userId", user.getId());
		claims.put("created",
				LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));

		return JwtUtil.generateToken(claims);
	}

	public static Long getUserIdByMailVerificationToken(String token) {
		Map<String, Object> claims = JwtUtil.getClaims(token);
		if (claims == null) {
			throw new NotAuthorizedException("Invalid token (" + token + ")");
		}

		if (!claims.get("sub").toString().equals("mail-verification")) {
			throw new NotAuthorizedException("Invalid token sub (" + token + ")");
		}

		return Long.parseLong(claims.get("userId").toString());
	}

	public static String generateMailUserChangePasswordToken(Users user, Long plusExpMinutes) {
		LocalDateTime now = LocalDateTime.now();

		Map<String, Object> claims = new HashMap<>();
		claims.put("sub", "mail-user-change-password");
		claims.put("iss", secretKey);
		claims.put("iat", Timestamp.valueOf(now));
		claims.put("exp", Timestamp.valueOf(now.plusMinutes(plusExpMinutes)).getTime() / 1000);
		claims.put("userId", user.getId());
		claims.put("created",
				LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));

		return JwtUtil.generateToken(claims);
	}

	public static Long getUserIdByMailUserChangePasswordToken(String token) {
		Map<String, Object> claims = JwtUtil.getClaims(token);
		if (claims == null) {
			throw new NotAuthorizedException("Invalid token (" + token + ")");
		}

		if (!claims.get("sub").toString().equals("mail-user-change-password")) {
			throw new NotAuthorizedException("Invalid token sub (" + token + ")");
		}

		return Long.parseLong(claims.get("userId").toString());
	}

}
