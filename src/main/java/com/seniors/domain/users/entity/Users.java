package com.seniors.domain.users.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.seniors.common.constant.OAuthProvider;
import com.seniors.common.entity.BaseTimeEntity;
import com.seniors.domain.chat.entity.ChatMessage;
import com.seniors.domain.chat.entity.ChatRoomMembers;
import com.seniors.domain.comment.entity.Comment;
import com.seniors.domain.notification.entity.Notification;
import com.seniors.domain.post.entity.Post;
import com.seniors.domain.post.entity.PostLike;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Users", indexes = {
		@Index(name = "idx_userId_snsId", columnList = "id, snsId")
})
public class Users extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(columnDefinition = "varchar(255) not null COMMENT 'sns 고유 ID'", unique = true)
	private String snsId;

	@Column(columnDefinition = "varchar(50) COMMENT '이메일'")
	private String email;

	@Column(columnDefinition = "varchar(30) not null COMMENT '닉네임'", unique = true)
	private String nickname;

	@Column(columnDefinition = "varchar(10) COMMENT '성별'")
	private String gender;

	@Column(columnDefinition = "varchar(10) COMMENT '생년월일'")
	private String birthday;

	@Column(columnDefinition = "varchar(20) COMMENT '나이 연령대'")
	private String ageRange;

	@Column(columnDefinition = "varchar(50) COMMENT '휴대전화번호'")
	private String phoneNumber;

	@Column(columnDefinition = "text COMMENT '프로필 이미지 url'")
	private String profileImageUrl;

	@Column(columnDefinition = "varchar(20) COMMENT 'OAuth Provider'")
	@Enumerated(EnumType.STRING)
	private OAuthProvider oAuthProvider;

	@OneToMany(mappedBy = "users", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<Post> posts = new ArrayList<>();

	@BatchSize(size = 100)
	@OneToMany(mappedBy = "users", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<Comment> comments = new ArrayList<>();

	@BatchSize(size = 100)
	@OneToMany(mappedBy = "users", cascade = CascadeType.PERSIST)
	private List<ChatMessage> chatMessages = new ArrayList<>();

	@JsonManagedReference
	@OneToMany(mappedBy = "users", cascade = CascadeType.PERSIST)
	private List<ChatRoomMembers> chatRoomMembers = new ArrayList<>();

	@BatchSize(size = 100)
	@OneToMany(mappedBy = "users", cascade = CascadeType.PERSIST)
	private List<PostLike> postLikes = new ArrayList<>();

	@JsonManagedReference
	@OneToMany(mappedBy = "users", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<Notification> notifications = new ArrayList<>();

	public static Users of(
			String snsId, String email, String nickname, OAuthProvider oAuthProvider,
			String gender, String birthday, String ageRange, String profileImageUrl
	) {
		return Users.builder()
				.snsId(snsId)
				.email(email)
				.nickname(nickname)
				.oAuthProvider(oAuthProvider)
				.gender(gender)
				.birthday(birthday)
				.ageRange(ageRange)
				.profileImageUrl(profileImageUrl)
				.build();
	}

	@Builder
	public Users (String snsId, String email, String nickname, OAuthProvider oAuthProvider,
	              String gender, String birthday, String ageRange, String profileImageUrl
	) {
		this.snsId = snsId;
		this.email = email;
		this.nickname = nickname;
		this.oAuthProvider = oAuthProvider;
		this.gender = gender;
		this.profileImageUrl = profileImageUrl;
		this.birthday = birthday;
		this.ageRange = ageRange;
	}
}
