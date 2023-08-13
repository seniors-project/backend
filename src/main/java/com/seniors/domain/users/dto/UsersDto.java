package com.seniors.domain.users.dto;

import com.seniors.domain.chat.dto.ChatRoomMembersDto.GetChatRoomMembersRes;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

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


}
