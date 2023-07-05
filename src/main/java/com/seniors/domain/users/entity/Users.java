package com.seniors.domain.users.entity;

import com.seniors.common.constant.OAuthProvider;
import com.seniors.domain.comment.Comment;
import com.seniors.domain.common.BaseEntity;
import com.seniors.domain.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(columnDefinition = "varchar(255) COMMENT 'sns 고유 ID'", unique = true)
	private String snsId;

	@Column(columnDefinition = "varchar(50) COMMENT '이메일'")
	private String email;

	@Column(columnDefinition = "varchar(30) COMMENT '닉네임'", unique = true)
	private String nickname;

	@Column(columnDefinition = "varchar(10) COMMENT '성별'")
	private String gender;

	@Column(columnDefinition = "varchar(50) COMMENT '휴대전화번호'")
	private String phoneNumber;

	@Column(columnDefinition = "text COMMENT '프로필 이미지 url'")
	private String profileImageUrl;

	@Column(columnDefinition = "varchar(20) COMMENT 'OAuth Provider'")
	private OAuthProvider oAuthProvider;

	@OneToMany(mappedBy = "users", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<Post> posts = new ArrayList<>();

	@OneToMany(mappedBy = "users", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<Comment> comments = new ArrayList<>();

	public static Users of(
			String snsId, String email, String nickname, OAuthProvider oAuthProvider
	) {
		return Users.builder()
				.snsId(snsId)
				.email(email)
				.nickname(nickname)
				.oAuthProvider(oAuthProvider)
				.build();
	}

	@Builder
	public Users (String snsId, String email, String nickname, OAuthProvider oAuthProvider) {
		this.snsId = snsId;
		this.email = email;
		this.nickname = nickname;
		this.oAuthProvider = oAuthProvider;
	}
}
