package com.seniors.domain.chat.dto;

import com.seniors.domain.users.dto.UsersDto;
import com.seniors.domain.users.entity.Users;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
public class ChatMessageDto {

    @Data
    @Builder
    public static class ChatMessageCreateDto {
        @Schema(description = "채팅 메세지", defaultValue = "내용 1", example = "내용 1입니다")
        @NotBlank(message = "Input message!")
        private String chatMessage;

        public static ChatMessageCreateDto from(String chatMessage) {
            return ChatMessageCreateDto.builder().chatMessage(chatMessage).build();
        }
    }

    @Data
    @Builder
    @AllArgsConstructor     //  DTO를 이용해서 메세지를 받을 때, 기본 생성자가 있어야 함.
    @NoArgsConstructor
    public static class ChatMessageTransDto {
        @Schema(description = "채팅방 ID")
        private Long chatRoomId;

        @Schema(description = "보내는 사람")
        private Long userId;

        @Schema(description = "메세지 내용")
        private String content;

        public static ChatMessageTransDto of(
                Long chatRoomId, Long userId,
                String content) {

            return ChatMessageTransDto.builder()
                    .chatRoomId(chatRoomId)
                    .userId(userId)
                    .content(content)
                    .build();
        }
    }

    @Data
    public static class GetChatMessageRes {
        @Schema(description = "메세지 ID")
        private Long chatMessageId;

        @Schema(description = "메시지 내용")
        private String content;

        @Schema(description = "생성 일자")
        private LocalDateTime createdAt;

        @Schema(description = "최근 수정 일자")
        private LocalDateTime lastModifiedDate;

        @Schema(description = "작성자")
        private UsersDto.GetPostUserRes users;

        public GetChatMessageRes(
                Long chatMessageId, String content,
                LocalDateTime createdAt, LocalDateTime lastModifiedDate,
                Users users
        ) {
            this.chatMessageId = chatMessageId;
            this.content = content;
            this.createdAt = createdAt;
            this.lastModifiedDate = lastModifiedDate;
            this.users = new UsersDto.GetPostUserRes(
                    users.getId(),
                    users.getGender(),
                    users.getNickname(),
                    users.getProfileImageUrl()
            );
        }
    }




}
