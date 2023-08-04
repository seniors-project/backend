package com.seniors.domain.notification.dto;

import com.seniors.domain.notification.entity.Notification;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationDto {
	/**
	 * 알림 id
	 */
	private Long id;
	private Long userId;

	/**
	 * 알림 내용
	 */
	private String content;

	/**
	 * 알림 클릭 시 이동할 url
	 */
	private String url;

	/**
	 * 알림이 생성된 날짜(몇일 전 계산 위함)
	 */
	private LocalDateTime createdAt;

	/**
	 * 알림 읽음 여부
	 */
	private boolean isRead;

	@Builder
	public NotificationDto(Long id, Long userId, String content, String url, LocalDateTime createdAt, boolean isRead) {
		this.id = id;
		this.userId = userId;
		this.content = content;
		this.url = url;
		this.createdAt = createdAt;
		this.isRead = isRead;
	}

	public static NotificationDto of(Notification notification) {
		return NotificationDto.builder()
				.id(notification.getId())
				.userId(notification.getUsers().getId())
				.content(notification.getContent())
				.url(notification.getUrl())
				.createdAt(notification.getCreatedAt())
				.isRead(notification.isRead())
				.build();
	}
}
