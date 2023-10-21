package com.seniors.domain.chat.service;

import com.seniors.common.dto.DataResponseDto;
import com.seniors.common.exception.type.NotAuthorizedException;
import com.seniors.domain.chat.dto.ChatRoomDto;
import com.seniors.domain.chat.entity.ChatRoom;
import com.seniors.domain.chat.entity.ChatRoomMembers;
import com.seniors.domain.chat.repository.ChatRoomMembersRepository;
import com.seniors.domain.chat.repository.ChatRoomRepository;
import com.seniors.domain.users.dto.UsersDto;
import com.seniors.domain.users.entity.Users;
import com.seniors.domain.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UsersRepository usersRepository;
    private final ChatRoomMembersRepository chatRoomMembersRepository;

    @Transactional
    public ChatRoomDto.GetChatRoomRes addChatRoom(Long userId, Long opponentId) {

        Optional<Long> chatRoomId = chatRoomMembersRepository.findChatRoomIdByUserIds(userId, opponentId);

        if (chatRoomId.get() == 0L) {
            ChatRoom chatRoom = ChatRoom.builder().build();
            chatRoomRepository.save(chatRoom);

            Users user = usersRepository.findById(userId).orElseThrow(
                    () -> new NotAuthorizedException("유효하지 않은 회원입니다")
            );
            Users opponentUser = usersRepository.findById(opponentId).orElseThrow(
                    () -> new NotAuthorizedException("유효하지 않은 회원입니다")
            );

            chatRoomMembersRepository.save(ChatRoomMembers.of(opponentUser.getNickname(), chatRoom, opponentUser));
            chatRoomMembersRepository.save(ChatRoomMembers.of(user.getNickname(), chatRoom, user));

            return chatRoomRepository.findOneChatRoom(chatRoom.getId());

        } else {
            return chatRoomRepository.findOneChatRoom(chatRoomId.orElseThrow());
        }

    }

    @Transactional(readOnly = true)
    public UsersDto.GetChatUserRes findChatRoom(Long userId) {
        UsersDto.GetChatUserRes getChatUserRes = chatRoomRepository.findAllChatRoom(userId);
        return getChatUserRes;
    }

    @Transactional
    public ChatRoomDto.GetChatRoomRes findOneChatRoom(Long roomId) {
        return chatRoomRepository.findOneChatRoom(roomId);
    }

    @Transactional
    public void removeChatRoom(Long chatRoomId, Long userId) {
        ChatRoomMembers chatRoomMembers = chatRoomMembersRepository.findByChatRoomIdAndUsersId(chatRoomId, userId).orElseThrow(
                () -> new NotAuthorizedException("채팅방이 존재하지 않거나 유효하지 않은 회원입니다")
        );
        chatRoomMembersRepository.delete(chatRoomMembers);
    }

}
