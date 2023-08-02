package com.seniors.domain.notification.controller;

import com.seniors.common.annotation.LoginUsers;
import com.seniors.config.security.CustomUserDetails;
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
}
