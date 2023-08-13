package com.seniors.domain.chat.service;

import com.seniors.common.dto.DataResponseDto;
import com.seniors.common.exception.type.NotFoundException;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UsersRepository usersRepository;
    private final ChatRoomMembersRepository chatRoomMembersRepository;

    @Transactional
    public void addChatRoom(Long userId, Long opponentId) {

        ChatRoom chatRoom = ChatRoom.builder().build();
        chatRoomRepository.save(chatRoom);

        Users user = usersRepository.findById(userId).orElseThrow();
        Users opponentUser = usersRepository.findById(opponentId).orElseThrow(
                () -> new NotFoundException("유효하지 않은 회원입니다")
        );

        chatRoomMembersRepository.save(ChatRoomMembers.of(user.getNickname(), chatRoom, user));
        chatRoomMembersRepository.save(ChatRoomMembers.of(opponentUser.getNickname(), chatRoom, opponentUser));


    }

    @Transactional(readOnly = true)
    public DataResponseDto<UsersDto.GetChatUserRes> findChatRoom(Long userId) {
        UsersDto.GetChatUserRes getChatUserRes = chatRoomRepository.findAllChatRoom(userId);
        return DataResponseDto.of(getChatUserRes);
    }

    @Transactional
    public ChatRoomDto.GetChatRoomRes findOneChatRoom(Long roomId) {
        return chatRoomRepository.findOneChatRoom(roomId);
    }

}
