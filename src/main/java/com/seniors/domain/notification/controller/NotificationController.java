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
//@RequestMapping("/api/notification")
public class NotificationController {

	private final NotificationService notificationService;

	/**
	 * @title 로그인 한 유저 sse 연결
	 */
	@GetMapping(value = "/subscribe", produces = "text/event-stream")
	public SseEmitter subscribe(
			@LoginUsers CustomUserDetails customUserDetails,
			@RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
		return notificationService.subscribe(customUserDetails.getUserId(), lastEventId);
	}

	/**
	 * @title 로그인 한 유저의 모든 알림 조회
	 */
	@GetMapping("/notifications")
	public DataResponseDto<CustomPage<NotificationDto>> notifications(
			@LoginUsers CustomUserDetails customUserDetails,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false) int size
	) {
		return DataResponseDto.of(notificationService.findAllById(customUserDetails, page > 0 ? page - 1 : 0, size));
	}

	/**
	 * @title 알림 읽음 상태 변경
	 */
	@PatchMapping("/notifications/{id}")
	public DataResponseDto<String> readNotification(@PathVariable Long id) {
		notificationService.readNotification(id);
		return DataResponseDto.of("Read Success");
	}
}
