package com.seniors.domain.auth.controller;

import com.seniors.common.dto.ErrorResponse;
import com.seniors.domain.auth.dto.AuthTokens;
import com.seniors.domain.auth.kakao.KakaoLoginParams;
import com.seniors.domain.auth.service.OAuthLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Tag(name = "소셜 인증", description = "소셜 API 명세서")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
	private final OAuthLoginService oAuthLoginService;

	@Operation(summary = "카카오 로그인/회원가입")
	@ApiResponse(responseCode = "401", description = "유효하지 않은 회원입니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
	@PostMapping("/kakao")
	public ResponseEntity<AuthTokens> loginKakao(@RequestBody KakaoLoginParams params) {
		return ResponseEntity.ok(oAuthLoginService.login(params));
	}
}
