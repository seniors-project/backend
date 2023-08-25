package com.seniors.domain.chat.dto;

import com.seniors.domain.chat.entity.ChatMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomMembersDto {
    @Data
    public static class GetChatRoomMembersRes {

        @Schema(description = "채팅방 ID")
        private Long roomId;

        @Schema(description = "상대방 ID")
        private Long userId;

        @Schema(description = "채팅방 이름")
        private String roomName;

        @Schema(description = "채팅 내용", defaultValue = "채팅", example = "안녕하세요")
        private ChatMessageDto.GetChatMessageRes chatMessageRes;

        public GetChatRoomMembersRes(Long roomId, Long userId, String roomName, ChatMessage chatMessage) {
            this.roomId = roomId;
            this.userId = userId;
            this.roomName = roomName;
            this.chatMessageRes = new ChatMessageDto.GetChatMessageRes(
                    chatMessage.getContent(),
                    chatMessage.getCreatedAt(),
                    chatMessage.getLastModifiedDate()
            );
        }
    }



}
