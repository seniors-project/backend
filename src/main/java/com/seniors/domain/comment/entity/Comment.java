package com.seniors.domain.comment.entity;

import com.seniors.common.entity.BaseTimeEntity;
import com.seniors.domain.post.entity.Post;
import com.seniors.domain.users.entity.Users;
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
@SQLDelete(sql = "UPDATE Comment SET isDeleted = true WHERE id = ?")
@Where(clause = "isDeleted = false")
public class Comment extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(columnDefinition = "text not null COMMENT '댓글 내용'")
	private String content;

	private boolean isDeleted = Boolean.FALSE;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "postId")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Post post;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private Users users;

	public static Comment of(String content, Post post, Users users) {
		return Comment.builder().content(content)
				.isDeleted(false).post(post).users(users).build();
	}

	@Builder
	public Comment(String content, Boolean isDeleted, Post post, Users users) {
		this.content = content;
		this.isDeleted = isDeleted;
		this.post = post;
		this.users = users;
	}
}
