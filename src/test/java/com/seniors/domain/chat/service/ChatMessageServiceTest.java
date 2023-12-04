package com.seniors.domain.chat.service;

import com.seniors.config.security.CustomUserDetails;
import com.seniors.domain.chat.dto.ChatMessageDto;
import com.seniors.domain.chat.entity.ChatRoom;
import com.seniors.domain.chat.repository.ChatMessageRepository;
import com.seniors.domain.chat.repository.ChatRoomRepository;
import com.seniors.domain.config.WithMockCustomUser;
import com.seniors.domain.users.entity.Users;
import com.seniors.domain.users.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@Transactional
@ActiveProfiles("dev")
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableWebSocketMessageBroker
@Slf4j
@WithMockCustomUser

class ChatMessageServiceTest {

    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    private Authentication authentication;
    @Autowired
    private ChatMessageService chatMessageService;
    private Users users;

    @Autowired
    private WebApplicationContext webApplicationContext;


    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();

        authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        users = usersRepository.getOneUsers(customUserDetails.getUserId());
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