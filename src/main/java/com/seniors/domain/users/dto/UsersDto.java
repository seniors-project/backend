package com.seniors.domain.users.dto;

import com.seniors.domain.chat.dto.ChatRoomMembersDto.GetChatRoomMembersRes;
import com.seniors.domain.resume.dto.CareerDto;
import com.seniors.domain.resume.dto.CertificateDto;
import com.seniors.domain.resume.dto.EducationDto;
import com.seniors.domain.resume.dto.ResumeDto;
import com.seniors.domain.resume.dto.ResumeDto.GetResumeByQueryDslRes;
import com.seniors.domain.resume.dto.ResumeDto.GetResumeRes;
import com.seniors.domain.resume.entity.Resume;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Setter
public class UsersDto {

	@Data
	public static class GetPostUserRes {
		@Schema(description = "사용자 ID")
		private Long userId;

		@Schema(description = "성별")
		private String gender;

		@Schema(description = "닉네임")
		private String nickname;

		@Schema(description = "프로필 이미지 url")
		private String profileImageUrl;

		public GetPostUserRes(Long userId, String gender, String nickname, String profileImageUrl) {
			this.userId = userId;
			this.gender = gender;
			this.nickname = nickname;
			this.profileImageUrl = profileImageUrl;
		}
	}
	@Data
	public static class GetChatUserOneRes {
		@Schema(description = "사용자 ID")
		private Long userId;

		public GetChatUserOneRes(Long userId) {
			this.userId = userId;
		}
	}

	@Data
	public static class GetChatUserRes {
		@Schema(description = "사용자 ID")
		private Long userId;

		@Schema(description = "채팅방 리스트")
		private List<GetChatRoomMembersRes> chatRoomMembers;

		public GetChatUserRes(Long userId, List<GetChatRoomMembersRes> chatRoomMembers) {
			this.userId = userId;
			this.chatRoomMembers = chatRoomMembers;

		}
	}


	@Data
	public static class GetUserDetailRes {
		@Schema(description = "사용자 ID")
		private Long userId;

		@Schema(description = "사용자 sns ID")
		private String snsId;

		@Schema(description = "닉네임")
		private String nickname;

		@Schema(description = "프로필 이미지 url")
		private String profileImageUrl;

		@Schema(description = "이메일")
		private String email;

		@Schema(description = "성별")
		private String gender;

		@Schema(description = "사용자 이력서")
		private GetResumeRes resume;

		public GetUserDetailRes(Resume resume, Long userId, String snsId, String nickname, String profileImageUrl, String email, String gender) {
			this.userId = userId;
			this.snsId = snsId;
			this.nickname = nickname;
			this.profileImageUrl = profileImageUrl;
			this.email = email;
			this.gender = gender;
			this.resume = new GetResumeRes(resume);
		}
	}


}
