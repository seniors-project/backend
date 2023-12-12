package com.seniors.domain.chat.controller;

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
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@ActiveProfiles("dev")
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableWebSocketMessageBroker
@Slf4j
@WithMockCustomUser
class ChatMessageControllerTest {

    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    private Authentication authentication;
    private Users users;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;
    static final String WEBSOCKET_SUB_URI = "/sub/chat/room/";
    static final String WEBSOCKET_PUB_URI = "/pub/chat/sendMessage";

    @LocalServerPort
    private int port;

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
    @DisplayName("채팅하기")
    public void chatMessageSend() throws Exception {

        // given
        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession stompSession = stompClient.connect("ws://localhost:" + port + "/api/chat", new StompSessionHandlerAdapter() {
        }).get(1, TimeUnit.SECONDS);

        ChatRoom chatRoom = new ChatRoom();
        chatRoomRepository.save(chatRoom);
        ChatMessageDto.ChatMessageTransDto chatMessageTransDto = ChatMessageDto.ChatMessageTransDto.of(chatRoom.getId(), users.getId(), "채팅 내용입니다.");

        CompletableFuture<ChatMessageDto.ChatMessageTransDto> subscribeFuture = new CompletableFuture<>();

        // when
        stompSession.send(WEBSOCKET_PUB_URI, chatMessageTransDto);
        stompSession.subscribe(WEBSOCKET_SUB_URI + chatMessageTransDto.getChatRoomId(), new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ChatMessageDto.ChatMessageTransDto.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                subscribeFuture.complete((ChatMessageDto.ChatMessageTransDto) payload);
            }
        });

        // then
        assertEquals(chatMessageTransDto.getContent(), subscribeFuture.get(10, TimeUnit.SECONDS).getContent());
        assertEquals(chatMessageTransDto.getContent(), chatMessageRepository.findAll().get(0).getContent());
    }
}