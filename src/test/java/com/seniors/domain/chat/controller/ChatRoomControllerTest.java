package com.seniors.domain.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seniors.common.constant.OAuthProvider;
import com.seniors.common.constant.ResultCode;
import com.seniors.config.security.CustomUserDetails;
import com.seniors.domain.chat.dto.ChatRoomDto;
import com.seniors.domain.chat.entity.ChatRoom;
import com.seniors.domain.chat.entity.ChatRoomMembers;
import com.seniors.domain.chat.repository.ChatRoomMembersRepository;
import com.seniors.domain.chat.repository.ChatRoomRepository;
import com.seniors.domain.chat.service.ChatRoomService;
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

import java.util.Random;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@ActiveProfiles("dev")
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Slf4j
@WithMockCustomUser
class ChatRoomControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ChatRoomService chatRoomService;
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private ChatRoomMembersRepository chatRoomMembersRepository;
    private Authentication authentication;
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
    @DisplayName("채팅방 전체 조회")
    void chatRoomList() throws Exception {

        // given
        Users users2 = Users.of("222", "test2@test2.com", "test2", OAuthProvider.KAKAO,
                "male", "12-31", "20~29", "profileImageUrl");
        Users users3 = Users.of("333", "test3@test3.com", "test3", OAuthProvider.KAKAO,
                "male", "10-31", "30~39", "profileImageUrl");
        try {
            chatRoomService.addChatRoom(users.getId(), users2.getId());
            chatRoomService.addChatRoom(users.getId(), users3.getId());
        } catch (Exception e) {
        }

        // expected
        mockMvc.perform(get("/api/chat/rooms")
                        .contentType(APPLICATION_JSON)
                        .principal(authentication)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("채팅방 생성")
    void chatRoomAdd() throws Exception {

        // given
        Users users2 = usersRepository.save(Users.of("222", "test2@test2.com", "test2", OAuthProvider.KAKAO,
                "male", "12-31", "20~29", "profileImageUrl"));

        ChatRoomDto.ChatRoomCreateDto chatRoomCreateDto = new ChatRoomDto.ChatRoomCreateDto();
        chatRoomCreateDto.setChatUserId(users2.getId());
        String json = objectMapper.writeValueAsString(chatRoomCreateDto);

        // expected
        mockMvc.perform(post("/api/chat/rooms")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                        .principal(authentication)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @DisplayName("채팅방 생성 실패")
    void failedChatRoomAdd() throws Exception {

        // given
        Random random = new Random();
        long randomNumber = random.nextLong() % 100L;
        if (randomNumber < 0) {
            randomNumber *= -1;
        }

        ChatRoomDto.ChatRoomCreateDto chatRoomCreateDto = new ChatRoomDto.ChatRoomCreateDto();
        chatRoomCreateDto.setChatUserId(randomNumber);

        String json = objectMapper.writeValueAsString(chatRoomCreateDto);

        // expected
        mockMvc.perform(post("/api/chat/rooms")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                        .principal(authentication)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(ResultCode.NOT_FOUND.getCode()))
                .andDo(print());
    }

    @Test
    @DisplayName("채팅방 입장")
    void chatRoomEnter() throws Exception {

        // given
        ChatRoom chatRoom = new ChatRoom();
        chatRoomRepository.save(chatRoom);
        chatRoomMembersRepository.save(ChatRoomMembers.of("user", chatRoom, users));

        // expected
        mockMvc.perform(get("/api/chat/rooms/{roomId}", chatRoom.getId())
                        .contentType(APPLICATION_JSON)
                        .principal(authentication)
                ).andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("채팅방 나가기")
    void chatRoomExit() throws Exception {

        // given
        ChatRoom chatRoom = new ChatRoom();
        chatRoomRepository.save(chatRoom);
        chatRoomMembersRepository.save(ChatRoomMembers.of("user", chatRoom, users));

        // expected
        mockMvc.perform(delete("/api/chat/rooms/{roomId}", chatRoom.getId())
                        .contentType(APPLICATION_JSON)
                        .principal(authentication)
                ).andExpect(status().isOk())
                .andDo(print());
    }
}