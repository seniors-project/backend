package com.seniors.domain.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomMembersDto {
    @Data
    public static class GetChatRoomMembersRes {
        @Schema(description = "채팅방 이름")
        private String roomName;

        public GetChatRoomMembersRes(String roomName) {
            this.roomName = roomName;
        }
    }



}
