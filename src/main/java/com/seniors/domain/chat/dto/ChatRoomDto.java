package com.seniors.domain.chat.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.seniors.domain.chat.entity.ChatMessage;
import com.seniors.domain.chat.entity.ChatRoomMembers;
import com.seniors.domain.users.dto.UsersDto.GetPostUserRes;
import com.seniors.domain.chat.dto.ChatMessageDto.GetChatMessageRes;
import com.seniors.domain.users.entity.Users;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomDto {

    @Data
    @Builder
    public static class ChatRoomCreateDto {
        @Schema(description = "채팅방 ID")
        private Long roomId;

        @Schema(description = "채팅방 이름")
        private String roomName;

        public static ChatRoomCreateDto of(Long roomId, String roomName) {
            return ChatRoomCreateDto.builder().roomId(roomId).roomName(roomName).build();
        }
    }


    @Data
    public static class GetChatRoomRes {
        @Schema(description = "채팅방 ID")
        private Long roomId;

        @Schema(description = "채팅방 이름")
        private String roomName;

        @Schema(description = "생성 일자")
        private LocalDateTime createdAt;

        @Schema(description = "최근 수정 일자")
        private LocalDateTime lastModifiedDate;

//        @Schema(description = "채팅방 생성자")
//        private List<ChatRoomMembers> chatRoomMembers;

        @Schema(description = "채팅 메세지")
        private List<GetChatMessageRes> chatMessages;

        @QueryProjection
        public GetChatRoomRes(Long roomId, String roomName,
                              LocalDateTime createdAt, LocalDateTime lastModifiedDate,
                              List<ChatMessage> chatMessages) {
            this.roomId = roomId;
            this.roomName = roomName;
            this.createdAt = createdAt;
            this.lastModifiedDate = lastModifiedDate;
            this.chatMessages = chatMessages.stream()
                    .map(chatMessage -> new GetChatMessageRes(
                            chatMessage.getId(),
                            chatMessage.getContent(),
                            chatMessage.getCreatedAt(),
                            chatMessage.getLastModifiedDate()

                    ))
                    .collect(Collectors.toList());
        }
    }

}
