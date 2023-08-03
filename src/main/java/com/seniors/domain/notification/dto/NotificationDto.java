package com.seniors.domain.notification.dto;

import com.seniors.common.util.LocalDateTimeToArray;
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
	private boolean read;

	@Builder
	public NotificationDto(Long id, String content, String url, LocalDateTime createdAt, boolean read) {
		this.id = id;
		this.content = content;
		this.url = url;
		this.createdAt = createdAt;
		this.read = read;
	}

	public static NotificationDto from(Notification notification) {
		return NotificationDto.builder()
				.id(notification.getId())
				.content(notification.getContent())
				.url(notification.getUrl())
				.createdAt(notification.getCreatedAt())
				.read(notification.isRead())
				.build();
	}
}
