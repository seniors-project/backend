package com.seniors.domain.post.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.seniors.domain.comment.entity.Comment;
import com.seniors.domain.users.entity.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.seniors.domain.common.BaseEntity;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "isDeleted = false")
@SQLDelete(sql = "UPDATE Post SET isDeleted = true WHERE id = ?")
public class Post extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(columnDefinition = "varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci not null COMMENT '게시글 제목'")
	private String title;

	@Column(columnDefinition = "text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci not null COMMENT '게시글 내용'")
	@Lob
	private String content;

	private boolean isDeleted = Boolean.FALSE;

	@Column(columnDefinition = "int unsigned not null default 0 COMMENT '게시글 좋아요 수'")
	private Integer likeCount;


	@BatchSize(size = 100)
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Comment> comments = new ArrayList<>();

	@BatchSize(size = 100)
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private Set<PostMedia> postMedias = new HashSet<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private Users users;

	@BatchSize(size = 100)
	@OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
	private Set<PostLike> postLikes = new HashSet<>();

	public static Post of(String title, String content, Users users) {
		return Post.builder()
				.title(title)
				.content(content)
				.isDeleted(false)
				.likeCount(0)
				.users(users)
				.build();
	}

	@Builder
	public Post(String title, String content, Boolean isDeleted, Integer likeCount, Users users) {
		this.title = title;
		this.content = content;
		this.isDeleted = isDeleted;
		this.likeCount = likeCount;
		this.users = users;
	}
}
