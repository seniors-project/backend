package com.seniors.api.comment;

import com.seniors.api.common.BaseEntity;
import com.seniors.api.post.domain.Post;
import com.seniors.api.users.domain.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE Comment SET isDeleted = true WHERE id = ?")
@Where(clause = "isDeleted = false")
public class Comment extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(columnDefinition = "varchar(20) not null COMMENT '댓글 사용자 이름'")
	private String nickname;

	@Column(columnDefinition = "text not null COMMENT '댓글 내용'")
	private String content;

//	@Column(columnDefinition = "tinyint(3) not null default 0 COMMENT '삭제 상태'")
//	private Integer isDeleted;
	private boolean isDeleted = Boolean.FALSE;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "postId")
	private Post post;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "userId")
	private Users users;

	public static Comment initComment(String nickname, String content, Post post, Users users) {
		return Comment.builder().nickname(nickname).content(content)
				.isDeleted(false).post(post).users(users).build();
	}

	@Builder
	public Comment(String nickname, String content, Boolean isDeleted, Post post, Users users) {
		this.nickname = nickname;
		this.content = content;
		this.isDeleted = isDeleted;
		this.post = post;
		this.users = users;
	}
}
