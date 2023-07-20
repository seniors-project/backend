package com.seniors.domain.chat.entity;

import com.seniors.domain.common.BaseEntity;
import com.seniors.domain.users.entity.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE Chat SET isDeleted = true WHERE id = ?")
@Where(clause = "isDeleted = false")
public class ChatMessage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci not null COMMENT '채팅 내용'")
    @Lob
    private String content;

    private boolean isDeleted = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private Users users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroomId")
    private ChatRoom chatRoom;

    public static ChatMessage from(String content) {
        return ChatMessage.builder().content(content).isDeleted(false).build();
    }

    @Builder
    public ChatMessage(String content, Boolean isDeleted) {
        this.content = content;
        this.isDeleted = isDeleted;
    }
}
