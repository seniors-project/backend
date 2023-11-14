package com.seniors.domain.chat.service;

import com.seniors.common.constant.OAuthProvider;
import com.seniors.config.security.CustomUserDetails;
import com.seniors.domain.chat.entity.ChatMessage;
import com.seniors.domain.chat.entity.ChatRoom;
import com.seniors.domain.chat.entity.ChatRoomMembers;
import com.seniors.domain.chat.repository.ChatMessageRepository;
import com.seniors.domain.chat.repository.ChatRoomMembersRepository;
import com.seniors.domain.chat.repository.ChatRoomRepository;
import com.seniors.domain.users.entity.Users;
import com.seniors.domain.users.repository.UsersRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@ActiveProfiles("dev")
@Slf4j
class ChatRoomServiceTest {

    @Autowired
    private ChatRoomService chatRoomService;
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private ChatRoomMembersRepository chatRoomMembersRepository;
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private EntityManager em;
    private Authentication authentication;
    private Users users;

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
    @DisplayName("채팅방 생성")
    void addChatRoom() {
        // given
        Users users2 = usersRepository.save(Users.builder()
                .snsId(String.valueOf(222))
                .email("test2@test2.com")
                .nickname("user2")
                .gender("male")
                .ageRange("30~39")
                .birthday("11-30")
                .oAuthProvider(OAuthProvider.KAKAO)
                .profileImageUrl("profileImageUrl")
                .build());

        // when
        chatRoomService.addChatRoom(users.getId(), users2.getId());;

        // then
        assertEquals(1L, chatRoomRepository.count());
        assertEquals(2L, chatRoomMembersRepository.count());
    }

    @Test
    @DisplayName("채팅방 전체 조회")
    void findChatRoom() {
        // given
        Users users2 = usersRepository.save(Users.builder()
                .snsId(String.valueOf(222))
                .email("test2@test2.com")
                .nickname("user2")
                .gender("male")
                .ageRange("30~39")
                .birthday("11-30")
                .oAuthProvider(OAuthProvider.KAKAO)
                .profileImageUrl("profileImageUrl")
                .build());

        Users users3 = usersRepository.save(Users.builder()
                .snsId(String.valueOf(333))
                .email("test3@test3.com")
                .nickname("user3")
                .gender("male")
                .ageRange("40~49")
                .birthday("10-30")
                .oAuthProvider(OAuthProvider.KAKAO)
                .profileImageUrl("profileImageUrl")
                .build());

        // when
        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.builder().build());
        ChatRoomMembers chatRoomMembers = chatRoomMembersRepository.save(ChatRoomMembers.of(users.getNickname(), chatRoom, users));
        ChatRoomMembers chatRoomMembers2 = chatRoomMembersRepository.save(ChatRoomMembers.of(users2.getNickname(), chatRoom, users2));

        ChatRoom chatRoom2 = chatRoomRepository.save(ChatRoom.builder().build());
        ChatRoomMembers chatRoomMembers3 = chatRoomMembersRepository.save(ChatRoomMembers.of(users.getNickname(), chatRoom2, users));
        ChatRoomMembers chatRoomMembers4 = chatRoomMembersRepository.save(ChatRoomMembers.of(users3.getNickname(), chatRoom2, users3));

        ChatMessage chatMessage = chatMessageRepository.save(ChatMessage.of(chatRoom, users, "유저1입니다"));
        ChatMessage chatMessage2 = chatMessageRepository.save(ChatMessage.of(chatRoom2, users, "유저1입니다"));

        // then
        assertEquals(2, chatRoomService.findChatRoom(users.getId()).getChatRoomMembers().size());
        assertEquals(1, chatRoomService.findChatRoom(users2.getId()).getChatRoomMembers().size());
        assertEquals(1, chatRoomService.findChatRoom(users3.getId()).getChatRoomMembers().size());

    }

    @Test
    @DisplayName("채팅방 입장")
    void findOneChatRoom() {
        // given
        Users users2 = usersRepository.save(Users.builder()
                .snsId(String.valueOf(222))
                .email("test2@test2.com")
                .nickname("user2")
                .gender("male")
                .ageRange("30~39")
                .birthday("11-30")
                .oAuthProvider(OAuthProvider.KAKAO)
                .profileImageUrl("profileImageUrl")
                .build());

        // when
        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.builder().build());
        ChatRoomMembers chatRoomMembers = chatRoomMembersRepository.save(ChatRoomMembers.of(users.getNickname(), chatRoom, users));
        ChatRoomMembers chatRoomMembers2 = chatRoomMembersRepository.save(ChatRoomMembers.of(users2.getNickname(), chatRoom, users2));

        ChatMessage chatMessage = chatMessageRepository.save(ChatMessage.of(chatRoom, users, "채팅방1 입니다"));

        em.flush();
        em.clear();

        // then
        assertEquals("채팅방1 입니다", chatRoomService.findOneChatRoom(chatRoom.getId(), users.getId()).getChatMessages().get(0).getContent());

    }

    @Test
    @DisplayName("채팅방 삭제")
    void removeChatRoom() {
        // given
        Users users2 = usersRepository.save(Users.builder()
                .snsId(String.valueOf(222))
                .email("test2@test2.com")
                .nickname("user2")
                .gender("male")
                .ageRange("30~39")
                .birthday("11-30")
                .oAuthProvider(OAuthProvider.KAKAO)
                .profileImageUrl("profileImageUrl")
                .build());

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.builder().build());
        ChatRoomMembers chatRoomMembers = chatRoomMembersRepository.save(ChatRoomMembers.of(users.getNickname(), chatRoom, users));

        // when
        chatRoomService.removeChatRoom(chatRoom.getId(), users.getId());

        // then
        assertEquals(0, chatRoomMembersRepository.count());
    }
}