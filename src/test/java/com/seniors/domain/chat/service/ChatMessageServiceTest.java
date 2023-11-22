package com.seniors.domain.chat.service;

import com.seniors.common.constant.OAuthProvider;
import com.seniors.config.security.CustomUserDetails;
import com.seniors.domain.chat.dto.ChatMessageDto;
import com.seniors.domain.chat.entity.ChatRoom;
import com.seniors.domain.chat.repository.ChatMessageRepository;
import com.seniors.domain.chat.repository.ChatRoomRepository;
import com.seniors.domain.users.entity.Users;
import com.seniors.domain.users.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableWebSocketMessageBroker
@ActiveProfiles("dev")
@Slf4j
class ChatMessageServiceTest {

    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    private Authentication authentication;
    @Autowired
    private ChatMessageService chatMessageService;
    private Users users;


    @BeforeEach
    void clean() {
        chatRoomRepository.deleteAll();
        usersRepository.deleteAll();

        users = usersRepository.save(Users.builder()
                .snsId(String.valueOf(123456))
                .email("test@test.com")
                .nickname("user1")
                .gender("male")
                .ageRange("20~29")
                .birthday("12-31")
                .oAuthProvider(OAuthProvider.KAKAO)
                .profileImageUrl("profileImageUrl")
                .build());

        CustomUserDetails userDetails = new CustomUserDetails(
                users.getId(),
                users.getSnsId(),
                users.getEmail(),
                users.getNickname(),
                users.getGender(),
                users.getProfileImageUrl());
        userDetails.setUserId(users.getId());

        authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    @Test
    @DisplayName("채팅 메세지 저장")
    public void saveChatMessage() {
        // given
        ChatRoom chatRoom = new ChatRoom();
        chatRoomRepository.save(chatRoom);
        ChatMessageDto.ChatMessageTransDto chatMessageTransDto = ChatMessageDto.ChatMessageTransDto.of(chatRoom.getId(), users.getId(), "채팅 테스트입니다");

        // when
        ChatMessageDto.GetChatMessageListRes chatMessageListRes = chatMessageService.saveChatMessage(chatMessageTransDto);

        // then
        assertEquals("채팅 테스트입니다", chatMessageRepository.findById(chatMessageListRes.getChatMessageId()).get().getContent());
    }
}