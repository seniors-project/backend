package com.seniors.domain.notification.controller;

import com.seniors.common.annotation.LoginUsers;
import com.seniors.common.dto.CustomPage;
import com.seniors.common.dto.DataResponseDto;
import com.seniors.config.security.CustomUserDetails;
import com.seniors.domain.notification.dto.NotificationDto;
import com.seniors.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

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
		return notificationService.subscribe(customUserDetails.getUserId(), lastEventId);
	}

	/**
	 * @title 로그인 한 유저의 모든 알림 조회
	 */
	@GetMapping("")
	public DataResponseDto<CustomPage<NotificationDto>> notificationList(
			@LoginUsers CustomUserDetails userDetails,
			@RequestParam int size,
			@RequestParam(required = false) Long lastId
	) {
		CustomPage<NotificationDto> notificationList = notificationService.findNotificationList(userDetails, size, lastId);
		return DataResponseDto.of(notificationList);
	}

	/**
	 * @title 알림 읽음 상태 변경
	 */
	@PatchMapping("/{id}")
	public DataResponseDto<String> notificationRead(
			@PathVariable Long id,
			@LoginUsers CustomUserDetails userDetails
	) {
		notificationService.readNotification(userDetails, id);
		return DataResponseDto.of("Read Success");
	}
}
