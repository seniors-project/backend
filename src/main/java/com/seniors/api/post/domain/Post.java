package com.seniors.api.post.domain;

import com.seniors.api.comment.Comment;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.seniors.api.common.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(columnDefinition = "varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci not null COMMENT '게시글 제목'")
	private String title;

	@Column(columnDefinition = "text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci not null COMMENT '게시글 내용'")
	@Lob
	private String content;

	@Column(columnDefinition = "tinyint(3) not null default 1 COMMENT '게시글 노출 여부'")
	private Integer display;

	@Column(columnDefinition = "tinyint(3) not null default 0 COMMENT '삭제 여부'")
	private Integer isDeleted;

	@Column(columnDefinition = "int unsigned not null default 0 COMMENT '게시글 조회 수'")
	private Integer viewCount;

	@Column(columnDefinition = "int unsigned not null default 0 COMMENT '게시글 좋아요 수'")
	private Integer likeCount;

	@OneToMany(mappedBy = "post", orphanRemoval = true, cascade = CascadeType.REMOVE)
	private List<Comment> comments = new ArrayList<>();
	public static Post initPost(String title, String content, Integer display) {
		return Post.builder().title(title).content(content).display(display).isDeleted(0).viewCount(0).likeCount(0).build();
	}

	@Builder
	public Post(String title, String content, Integer display, Integer isDeleted, Integer viewCount, Integer likeCount, List<Comment> comments) {
		this.title = title;
		this.content = content;
		this.display = display;
		this.isDeleted = isDeleted;
		this.viewCount = viewCount;
		this.likeCount = likeCount;
	}
}
