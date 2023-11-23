package com.seniors.domain.users.controller;

import com.seniors.common.annotation.LoginUsers;
import com.seniors.common.dto.DataResponseDto;
import com.seniors.common.dto.ErrorResponse;
import com.seniors.common.exception.type.BadRequestException;
import com.seniors.common.exception.type.NotAuthorizedException;
import com.seniors.common.exception.type.NotFoundException;
import com.seniors.config.security.CustomUserDetails;
import com.seniors.domain.post.dto.PostDto;
import com.seniors.domain.users.dto.UsersDto.GetUserDetailRes;
import com.seniors.domain.users.dto.UsersDto.SetUserDto;
import com.seniors.domain.users.service.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "사용자", description = "사용자 API 명세서")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UsersController {

	private final UsersService usersService;

	/**
	 * 유저 검증 API
	 * ex) 글쓰기 페이지 접속 시 검증 API를 사용하여
	 * 401 에러면 로그인 유도할 수 있도록 checking
	 * @param userDetails
	 * @return ResponseEntity
	 */
	@Operation(summary = "사용자 검증")
	@ApiResponse(responseCode = "200", description = "검증 성공",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class)))
	@ApiResponse(responseCode = "401", description = "검증 실패",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
	@GetMapping("/validate")
	public ResponseEntity<?> userValidate(
			@Parameter(hidden = true) @LoginUsers CustomUserDetails userDetails
	) {
		return userDetails == null
				? new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED)
				: new ResponseEntity<>(userDetails, HttpStatus.OK);
	}

	@Operation(summary = "유저 정보 조회")
	@ApiResponse(responseCode = "200", description = "단건 조회 성공",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetUserDetailRes.class)))
	@ApiResponse(responseCode = "401", description = "유효하지 않은 회원입니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
	@ApiResponse(responseCode = "404", description = "사용자가 존재하지 않습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
	@ApiResponse(responseCode = "500", description = "서버 에러.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
	@GetMapping("")
	public DataResponseDto<GetUserDetailRes> usersDetails(
			@Parameter(hidden = true) @LoginUsers CustomUserDetails userDetails
	) {
		GetUserDetailRes getUserRes = usersService.findOneUsers(userDetails.getUserId(), userDetails.getUserSnsId(),
				userDetails.getUserNickname(), userDetails.getProfileImageUrl(),
				userDetails.getUserEmail(), userDetails.getGender());
		return DataResponseDto.of(getUserRes);
	}

	@Operation(summary = "유저 정보 수정")
	@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "수정 요청 body",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = SetUserDto.class)))
	@ApiResponse(responseCode = "200", description = "단건 수정 성공",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponseDto.class)))
	@ApiResponse(responseCode = "400", description = "유효성 검증 실패",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
	@ApiResponse(responseCode = "401", description = "유효하지 않은 회원입니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
	@ApiResponse(responseCode = "404", description = "사용자가 존재하지 않습니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
	@ApiResponse(responseCode = "500", description = "서버 에러.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
	@PatchMapping("")
	public DataResponseDto<?> usersModify(
			@Parameter(hidden = true) @LoginUsers CustomUserDetails userDetails,
			@RequestPart(value = "data") SetUserDto setUserDto,
			@RequestPart(value = "profileImage", required = false) MultipartFile profileImage
	) throws IOException {
		usersService.modifyUsers(userDetails.getUserId(), userDetails.getProfileImageUrl(), setUserDto, profileImage);
		return DataResponseDto.of("SUCCESS");
	}

}
