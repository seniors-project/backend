package com.seniors.domain.notification.entity;

import com.seniors.domain.common.BaseTimeEntity;
import com.seniors.domain.users.entity.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Notification extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", insertable = false, updatable = false)
	private Users users;

	private String content;

	private String url;

	private boolean isRead;

	@Builder
	public Notification(Users users, String content, String url, boolean isRead) {
		this.users = users;
		this.content = content;
		this.url = url;
		this.isRead = isRead;
	}

	public void read() {
		this.isRead = true;
	}
}
