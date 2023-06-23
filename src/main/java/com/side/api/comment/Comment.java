package com.side.api.comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.side.api.common.BaseEntity;
import com.side.api.post.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(columnDefinition = "varchar(20) not null COMMENT '댓글 사용자 이름'")
	private String username;

	@Column(columnDefinition = "text not null COMMENT '댓글 내용'")
	private String content;

	@Column(columnDefinition = "tinyint(3) not null default 0 COMMENT '삭제 상태'")
	private Integer isDeleted;

	@Column(columnDefinition = "tinyint(3) not null default 0 COMMENT '비밀 댓글 상태'")
	private Integer secretStatus;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "postId")
	@JsonIgnore
	private Post post;

	public static Comment initComment(String username, String content, Integer secretStatus, Post post) {
//        isDeleted = isDeleted == 1 ? 0 : isDeleted;
		return Comment.builder().username(username).content(content)
				.secretStatus(secretStatus).isDeleted(0).post(post).build();
	}

	@Builder
	public Comment(String username, String content, Integer secretStatus, Integer isDeleted, Post post) {
		this.username = username;
		this.content = content;
		this.secretStatus = secretStatus;
		this.isDeleted = isDeleted;
		this.post = post;
	}
}
