package com.seniors.domain.post.entity;

import com.seniors.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "isDeleted = false")
@SQLDelete(sql = "UPDATE PostMedia SET isDeleted = true WHERE id = ?")
public class PostMedia extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(columnDefinition = "text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci not null COMMENT '게시글 이미지 및 동영상 url'")
	private String mediaUrl;

	private boolean isDeleted = Boolean.FALSE;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "postId")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Post post;

	public static PostMedia of(String mediaUrl, Post post) {
		return PostMedia.builder()
				.mediaUrl(mediaUrl)
				.isDeleted(false)
				.post(post)
				.build();
	}

	@Builder
	public PostMedia(String mediaUrl, Boolean isDeleted, Post post) {
		this.mediaUrl = mediaUrl;
		this.isDeleted = isDeleted;
		this.post = post;
	}
}
