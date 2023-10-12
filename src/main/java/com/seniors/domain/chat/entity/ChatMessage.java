package com.seniors.domain.chat.entity;

import com.seniors.common.entity.BaseEntity;
import com.seniors.domain.users.entity.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci not null COMMENT '채팅 내용'")
    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private Users users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroomId")
    private ChatRoom chatRoom;

    public static ChatMessage of(ChatRoom chatRoom, Users users, String content) {
        return ChatMessage.builder()
                .chatRoom(chatRoom)
                .users(users)
                .content(content)
                .build();
    }

    @Builder
    public ChatMessage(String content, ChatRoom chatRoom, Users users) {
        this.chatRoom = chatRoom;
        this.users = users;
        this.content = content;
    }
}
