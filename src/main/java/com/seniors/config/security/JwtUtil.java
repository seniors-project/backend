package com.seniors.config.security;

import com.google.common.base.Strings;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Slf4j
@Component
public class JwtUtil {
	private final SecretKey secretKey;
	private SecretKey refreshKey;
	private byte[] secretBytes;
	private byte[] refreshBytes;

	public JwtUtil(@Value("${jwt.secret-key}") String secretKey) {
		this.secretKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
		this.secretBytes = Base64.getDecoder().decode(secretKey.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * JWT 생성
	 *
	 * @param claims
	 * @return
	 */
	public String generateToken(Map<String, Object> claims, SecretKey key) {
		return Jwts.builder()
				.setClaims(claims)
				.signWith(key)
				.compact();
	}

	public String generateRefreshToken(Map<String, Object> claims, SecretKey key) {
		return Jwts.builder()
				.setClaims(claims)
				.signWith(key)
				.compact();
	}

	/**
	 * JWT 검증 및 데이터 가져오기
	 *
	 * @param jwt
	 * @return
	 */
	public Map<String, Object> getClaims(String jwt) {
		if (Strings.isNullOrEmpty(jwt)) {
			return null;
		}
		return Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(jwt)
				.getBody();
	}

	public Map<String, Object> getClaimsForReFresh(String jwt) {
		if (Strings.isNullOrEmpty(jwt)) {
			return null;
		}
		return Jwts.parserBuilder()
				.setSigningKey(refreshKey)
				.build()
				.parseClaimsJws(jwt)
				.getBody();
	}
}
