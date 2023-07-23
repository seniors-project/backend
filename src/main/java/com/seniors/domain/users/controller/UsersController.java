package com.seniors.domain.users.controller;

import com.seniors.common.annotation.LoginUsers;
import com.seniors.common.constant.ResultCode;
import com.seniors.common.dto.DataResponseDto;
import com.seniors.common.dto.ResponseDto;
import com.seniors.config.security.CustomUserDetails;
import com.seniors.domain.users.service.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UsersController {

	@Operation(summary = "유저 검증")
	@ApiResponse(responseCode = "200", description = "검증 성공",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class)))
	@ApiResponse(responseCode = "401", description = "검증 실패",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class)))
	@GetMapping("/validate")
	public ResponseEntity<?> userValidate(
			@LoginUsers CustomUserDetails userDetails
	) {
		return userDetails == null
				? new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED)
				: new ResponseEntity<>(userDetails, HttpStatus.OK);
	}


}
