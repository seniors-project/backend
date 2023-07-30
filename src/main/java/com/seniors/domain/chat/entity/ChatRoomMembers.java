package com.seniors.domain.chat.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.seniors.domain.common.BaseEntity;
import com.seniors.domain.users.entity.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@SQLDelete(sql = "UPDATE ChatRoomMembers SET isDeleted = true WHERE id = ?")
//@Where(clause = "isDeleted = false")
public class ChatRoomMembers extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "varchar(30) COMMENT '채팅방 이름'")
    private String roomName;

//    private boolean isDeleted = Boolean.FALSE;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroomId")
    private ChatRoom chatRoom;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private Users users;

    public static ChatRoomMembers of(String roomName, ChatRoom chatRoom, Users users) {
        return ChatRoomMembers.builder()
                .roomName(roomName)
                .chatRoom(chatRoom)
                .users(users)
                .build();
    }

    @Builder
    public ChatRoomMembers(String roomName, ChatRoom chatRoom, Users users) {
        this.roomName = roomName;
        this.chatRoom = chatRoom;
        this.users = users;
    }

}
