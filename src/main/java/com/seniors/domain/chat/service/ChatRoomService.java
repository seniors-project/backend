package com.seniors.domain.chat.service;

import com.seniors.common.dto.CustomPage;
import com.seniors.common.exception.type.BadRequestException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public void addChatRoom(Long senderId, String chatRoomName, Long recipientId) {
        if (chatRoomName == null) {
            throw new BadRequestException("ChatRoomName is required");
        }

        ChatRoom chatRoom = ChatRoom.builder().build();
        chatRoomRepository.save(chatRoom);

        Users users = usersRepository.findById(senderId).orElseThrow();
        chatRoomMembersRepository.save(ChatRoomMembers.of(chatRoomName, chatRoom, users));

        users = usersRepository.findById(recipientId).orElseThrow();
        chatRoomMembersRepository.save(ChatRoomMembers.of(chatRoomName, chatRoom, users));


    }

    @Transactional(readOnly = true)
    public CustomPage<UsersDto.GetChatUserRes> findChatRoom(Long userId, int page, int size) {
        Sort.Direction direction = Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "id"));
        Page<UsersDto.GetChatUserRes> getChatUserRes = usersRepository.findAllChatRoom(userId, pageable);
        return CustomPage.of(getChatUserRes);
    }

    @Transactional
    public ChatRoomDto.GetChatRoomRes findOneChatRoom(Long roomId) {
        return chatRoomRepository.findOneChatRoom(roomId);
    }

}
