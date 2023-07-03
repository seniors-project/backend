package com.seniors.api.post.domain;

import com.seniors.api.comment.Comment;
import com.seniors.api.users.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.seniors.api.common.BaseEntity;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE Post SET isDeleted = true WHERE id = ?")
@Where(clause = "isDeleted = false")
public class Post extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(columnDefinition = "varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci not null COMMENT '게시글 제목'")
	private String title;

	@Column(columnDefinition = "text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci not null COMMENT '게시글 내용'")
	@Lob
	private String content;

//	@Column(columnDefinition = "tinyint(3) not null default 0 COMMENT '삭제 여부'")
//	private Integer isDeleted;
	private boolean isDeleted = Boolean.FALSE;

	@Column(columnDefinition = "int unsigned not null default 0 COMMENT '게시글 조회 수'")
	private Integer viewCount;

	@Column(columnDefinition = "int unsigned not null default 0 COMMENT '게시글 좋아요 수'")
	private Integer likeCount;

	@OneToMany(mappedBy = "post", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<Comment> comments = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "userId")
	private Users users;

	public static Post initPost(String title, String content, Users users) {
		return Post.builder().title(title).content(content).isDeleted(false).viewCount(0).likeCount(0).users(users).build();
	}

	@Builder
	public Post(String title, String content, Boolean isDeleted, Integer viewCount, Integer likeCount, Users users) {
		this.title = title;
		this.content = content;
		this.isDeleted = isDeleted;
		this.viewCount = viewCount;
		this.likeCount = likeCount;
		this.users = users;
	}
}
