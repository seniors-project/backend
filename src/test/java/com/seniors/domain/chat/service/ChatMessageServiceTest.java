package com.seniors.domain.chat.service;

import com.seniors.common.constant.OAuthProvider;
import com.seniors.config.security.CustomUserDetails;
import com.seniors.domain.chat.dto.ChatMessageDto;
import com.seniors.domain.chat.entity.ChatRoom;
import com.seniors.domain.chat.repository.ChatMessageRepository;
import com.seniors.domain.chat.repository.ChatRoomMembersRepository;
import com.seniors.domain.chat.repository.ChatRoomRepository;
import com.seniors.domain.users.entity.Users;
import com.seniors.domain.users.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
    private ChatRoomMembersRepository chatRoomMembersRepository;
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    private Authentication authentication;
    private Users users;
    static final String WEBSOCKET_SUB_URI = "/sub/chat/room/";
    static final String WEBSOCKET_PUB_URI = "/pub/chat/sendMessage";

    @LocalServerPort
    private int port;

    @BeforeEach
    void clean() {
        chatRoomRepository.deleteAll();
        chatRoomMembersRepository.deleteAll();
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
    public void testWebSocketCommunication() throws InterruptedException, TimeoutException, ExecutionException {
        // given
        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        StompSessionHandler sessionHandler = new TestSessionHandler();

        StompSession stompSession = stompClient.connect("ws://localhost:" + port + "/api/chat", sessionHandler).get(5, TimeUnit.SECONDS);

        ChatRoom chatRoom = new ChatRoom();
        chatRoomRepository.save(chatRoom);

        // when
        ChatMessageDto.ChatMessageTransDto chatMessageTransDto = ChatMessageDto.ChatMessageTransDto.of(chatRoom.getId(), users.getId(), "채팅 내용입니다.");

        stompSession.subscribe(WEBSOCKET_SUB_URI + chatRoom.getId(), new TestStompFrameHandler());
        stompSession.send(WEBSOCKET_PUB_URI, chatMessageTransDto);

        Thread.sleep(1000);

        // then
        assertEquals("채팅 내용입니다.", chatMessageRepository.findAll().get(0).getContent());
    }

    private class TestStompFrameHandler implements StompFrameHandler {
        @Override
        public Type getPayloadType(StompHeaders headers) {
            return ChatMessageDto.ChatMessageCreateDto.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
        }
    }

    private class TestSessionHandler extends StompSessionHandlerAdapter {
        // 연결이 열릴 때의 동작을 정의
        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            log.info("Connected to the server");
        }
    }
}