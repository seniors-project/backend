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

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE Chat SET isDeleted = true WHERE id = ?")
@Where(clause = "isDeleted = false")
public class ChatRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "varchar(30) COMMENT '채팅방 이름'")
    private String roomName;

    private boolean isDeleted = Boolean.FALSE;

    @OneToMany(mappedBy = "chatRoom", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<ChatRoomMembers> chatRoomMembers = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoom", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<ChatMessage> chatMessages = new ArrayList<>();

    public static ChatRoom of(String roomName) {
        return ChatRoom.builder().roomName(roomName).isDeleted(false).build();
    }

    @Builder
    public ChatRoom(String roomName, Boolean isDeleted) {
        this.roomName = roomName;
        this.isDeleted = isDeleted;
    }
}
