package com.side.api.comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.side.api.common.BaseEntity;
import com.side.api.post.domain.Post;
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
	private String nickname;

	@Column(columnDefinition = "text not null COMMENT '댓글 내용'")
	private String content;

	@Column(columnDefinition = "tinyint(3) not null default 0 COMMENT '삭제 상태'")
	private Integer isDeleted;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "postId")
	@JsonIgnore
	private Post post;

	public static Comment initComment(String nickname, String content,  Post post) {
		return Comment.builder().nickname(nickname).content(content)
				.isDeleted(0).post(post).build();
	}

	@Builder
	public Comment(String nickname, String content, Integer isDeleted, Post post) {
		this.nickname = nickname;
		this.content = content;
		this.isDeleted = isDeleted;
		this.post = post;
	}
}
