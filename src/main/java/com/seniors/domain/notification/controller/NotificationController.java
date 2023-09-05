package com.seniors.domain.notification.controller;

import com.seniors.common.annotation.LoginUsers;
import com.seniors.common.dto.CustomPage;
import com.seniors.common.dto.DataResponseDto;
import com.seniors.common.dto.ErrorResponse;
import com.seniors.common.exception.type.BadRequestException;
import com.seniors.common.exception.type.NotAuthorizedException;
import com.seniors.common.exception.type.NotFoundException;
import com.seniors.config.security.CustomUserDetails;
import com.seniors.domain.notification.dto.NotificationDto;
import com.seniors.domain.notification.entity.Notification;
import com.seniors.domain.notification.service.NotificationService;
import com.seniors.domain.post.dto.PostDto;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "알림", description = "알림API 명세서")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

	private final NotificationService notificationService;

	/**
	 * @title 로그인 한 유저 sse 연결
	 */
	@GetMapping(value = "/subscribe", produces = "text/event-stream")
	public SseEmitter subscribeNotification(
			@LoginUsers CustomUserDetails customUserDetails,
			@RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
		return notificationService.notificationSubscribe(customUserDetails.getUserId(), lastEventId);
	}

	/**
	 * @title 로그인 한 유저의 모든 알림 조회
	 */
	@Operation(summary = "알림 리스트 조회")
	@ApiResponse(responseCode = "200", description = "리스트 조회 성공",
			content = @Content(mediaType = "application/json", schema =
			@Schema(implementation = CustomPage.class)))
	@ApiResponse(responseCode = "401", description = "유효하지 않은 회원입니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotAuthorizedException.class)))
	@ApiResponse(responseCode = "404", description = "알림이 존재하지 않습니다",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotFoundException.class)))
	@ApiResponse(responseCode = "500", description = "서버 에러.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
	@GetMapping("")
	public DataResponseDto<CustomPage<NotificationDto>> notificationList(
			@Parameter(hidden = true) @LoginUsers CustomUserDetails userDetails,
			@RequestParam int size,
			@RequestParam(required = false) Long lastId
	) {
		CustomPage<NotificationDto> notificationList = notificationService.findNotificationList(userDetails, size, lastId);
		return DataResponseDto.of(notificationList);
	}

	/**
	 * @title 알림 읽음 상태 변경
	 */
	@Operation(summary = "알림 단건 조회")
	@ApiResponse(responseCode = "200", description = "단건 조회 성공",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostDto.GetPostRes.class)))
	@ApiResponse(responseCode = "401", description = "유효하지 않은 회원입니다.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotAuthorizedException.class)))
	@ApiResponse(responseCode = "404", description = "알림이 존재하지 않습니다",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotFoundException.class)))
	@ApiResponse(responseCode = "500", description = "서버 에러.",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
	@PatchMapping("/{id}")
	public DataResponseDto<Notification> notificationRead(
			@PathVariable Long id,
			@LoginUsers CustomUserDetails userDetails
	) {
		return DataResponseDto.of(notificationService.readNotification(userDetails, id));
	}
}
