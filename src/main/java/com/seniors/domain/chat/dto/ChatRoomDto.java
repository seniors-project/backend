package com.seniors.domain.chat.dto;

import com.seniors.domain.chat.dto.ChatMessageDto.GetChatMessageRes;
import com.seniors.domain.chat.entity.ChatMessage;
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
    public static class ChatRoomCreateDto {
        @Schema(description = "상대방 ID")
        private Long chatUserId;

    }


    @Data
    public static class GetChatRoomRes {
        @Schema(description = "채팅방 ID")
        private Long roomId;

        @Schema(description = "생성 일자")
        private LocalDateTime createdAt;

        @Schema(description = "최근 수정 일자")
        private LocalDateTime lastModifiedDate;

        @Schema(description = "채팅 메세지")
        private List<ChatMessageDto.GetChatMessageListRes> chatMessages;

        public GetChatRoomRes(Long roomId, LocalDateTime createdAt,
                              LocalDateTime lastModifiedDate, List<ChatMessage> chatMessages) {
            this.roomId = roomId;
            this.createdAt = createdAt;
            this.lastModifiedDate = lastModifiedDate;
            this.chatMessages = chatMessages.stream()
                    .map(chatMessage -> new ChatMessageDto.GetChatMessageListRes(
                            chatMessage.getId(),
                            chatMessage.getContent(),
                            chatMessage.getCreatedAt(),
                            chatMessage.getLastModifiedDate(),
                            chatMessage.getUsers()
                    )).collect(Collectors.toList());

        }
    }



}
