package com.seniors.config.security;

import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.Map;

public class JwtUtil {

	private static String secretKey;
	private static String refreshKey;
	private static byte[] secretBytes;
	private static byte[] refreshBytes;

	public JwtUtil(String secretKey, String refreshKey) throws UnsupportedEncodingException {
		this.secretKey = secretKey;
		this.refreshKey = refreshKey;
		this.secretBytes = secretKey.getBytes("UTF-8");
		this.refreshBytes = refreshKey.getBytes("UTF-8");
	}

	/**
	 * JWT 생성
	 *
	 * @param claims
	 * @return
	 */
	public static String generateToken(Map<String, Object> claims) {

		return Jwts.builder()
				.setClaims(claims)
				.signWith(SignatureAlgorithm.HS256, secretBytes)
				.compact();
	}

	public static String generateRefreshToken(Map<String, Object> claims) {

		return Jwts.builder()
				.setClaims(claims)
				.signWith(SignatureAlgorithm.HS256, refreshBytes)
				.compact();
	}

	public static String generateToken(Map<String, Object> header,
	                                   Map<String, Object> claims, Key key) {
		return Jwts.builder()
				.setHeader(header)
				.setClaims(claims)
				.signWith(SignatureAlgorithm.valueOf(header.get(JwsHeader.ALGORITHM).toString()), key)
				.compact();
	}

	/**
	 * JWT 검증 및 데이터 가져오기
	 *
	 * @param jwt
	 * @return
	 */
	public static Map<String, Object> getClaims(String jwt) {
		if (Strings.isNullOrEmpty(jwt)) {
			return null;
		}
		Claims claims = Jwts.parser()
				.setSigningKey(secretBytes)
				.parseClaimsJws(jwt)
				.getBody();
		return claims;
	}

	public static Map<String, Object> getClaimsForReFresh(String jwt) {
		if (Strings.isNullOrEmpty(jwt)) {
			return null;
		}

		Claims claims = Jwts.parser()
				.setSigningKey(refreshBytes)
				.parseClaimsJws(jwt)
				.getBody();
		return claims;
	}
}
