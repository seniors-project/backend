package com.seniors.domain.chat.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.seniors.domain.chat.entity.ChatRoom;
import com.seniors.domain.users.dto.UsersDto.GetPostUserRes;
import com.seniors.domain.chat.dto.ChatRoomDto.GetChatRoomRes;
import com.seniors.domain.users.entity.Users;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    public static class GetChatMessageRes {
        @Schema(description = "메세지 ID")
        private Long chatMessageId;

        @Schema(description = "메시지 내용")
        private String content;

        @Schema(description = "생성 일자")
        private LocalDateTime createdAt;

        @Schema(description = "최근 수정 일자")
        private LocalDateTime lastModifiedDate;

//        @Schema(description = "작성자")
//        private GetPostUserRes users;

        @QueryProjection
        public GetChatMessageRes(Long chatMessageId, String content,
                                 LocalDateTime createdAt, LocalDateTime lastModifiedDate) {
            this.chatMessageId = chatMessageId;
            this.content = content;
            this.createdAt = createdAt;
            this.lastModifiedDate = lastModifiedDate;
//            this.users = new GetPostUserRes(
//                    users.getId(),
//                    users.getGender(),
//                    users.getNickname(),
//                    users.getProfileImageUrl()
//            );
        }
    }




}
