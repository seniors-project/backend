package com.seniors.domain.notification.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.seniors.common.entity.BaseTimeEntity;
import com.seniors.domain.users.entity.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "Notification", indexes = {
		@Index(name = "idx_userId", columnList = "userId"),
		@Index(name = "idx_notificationId_userId", columnList = "id, userId"),
})
public class Notification extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	@JsonBackReference
	private Users users;

	@Column(columnDefinition = "varchar(50) not null COMMENT '알림 내용'")
	private String content;

	@Column(columnDefinition = "varchar(100) not null COMMENT '알림 redirect API url'")
	private String url;

	private boolean isRead = Boolean.FALSE;

	public static Notification of(Users users, String content, String url, Boolean isRead) {
		return Notification.builder()
				.users(users)
				.content(content)
				.url(url)
				.isRead(isRead)
				.build();
	}

	@Builder
	public Notification(Users users, String content, String url, Boolean isRead) {
		this.users = users;
		this.content = content;
		this.url = url;
		this.isRead = isRead;
	}

	public void read() {
		this.isRead = true;
	}
}
