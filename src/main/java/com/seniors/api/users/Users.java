package com.seniors.api.users;

import com.seniors.api.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

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

	@Column(columnDefinition = "varchar(50) COMMENT '비밀번호'")
	private String password;

	@Column(columnDefinition = "varchar(30) COMMENT '닉네임'", unique = true)
	private String nickname;

	@Column(columnDefinition = "varchar(10) COMMENT '성별'")
	private String gender;

	@Column(columnDefinition = "varchar(50) COMMENT '휴대전화번호'")
	private String phoneNumber;

	public static Users initSnsUsers(
			String snsId, String email, String nickname
	) {
		return Users.builder()
				.snsId(snsId)
				.email(email)
				.nickname(nickname)
				.build();
	}

	@Builder
	public Users (String snsId, String email, String nickname) {
		this.snsId = snsId;
		this.email = email;
		this.nickname = nickname;
	}
}
