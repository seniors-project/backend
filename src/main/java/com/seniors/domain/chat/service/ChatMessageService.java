package com.seniors.domain.chat.service;

import com.seniors.common.exception.type.NotAuthorizedException;
import com.seniors.domain.chat.dto.ChatMessageDto;
import com.seniors.domain.chat.entity.ChatMessage;
import com.seniors.domain.chat.entity.ChatRoom;
import com.seniors.domain.chat.repository.ChatMessageRepository;
import com.seniors.domain.chat.repository.ChatRoomRepository;
import com.seniors.domain.users.entity.Users;
import com.seniors.domain.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final UsersRepository usersRepository;
    private final ChatMessageRepository chatMessageRepository;


    @Transactional
    public void saveChatMessage (ChatMessageDto.ChatMessageTransDto chat) {
        Users users = usersRepository.findById(chat.getUserId()).orElseThrow(
                () -> new NotAuthorizedException("유효하지 않은 회원입니다.")
        );
        ChatRoom chatRoom = chatRoomRepository.findById(chat.getChatRoomId()).orElseThrow();
        ChatMessage chatMessage = ChatMessage.of(chatRoom, users, chat.getContent());
        chatMessageRepository.save(chatMessage);

    }

}
