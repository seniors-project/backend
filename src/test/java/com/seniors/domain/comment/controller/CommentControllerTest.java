package com.seniors.domain.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seniors.common.constant.OAuthProvider;
import com.seniors.config.security.CustomUserDetails;
import com.seniors.domain.comment.dto.CommentDto;
import com.seniors.domain.comment.entity.Comment;
import com.seniors.domain.comment.repository.CommentRepository;
import com.seniors.domain.post.dto.PostDto;
import com.seniors.domain.post.dto.PostDto.PostCreateDto;
import com.seniors.domain.post.entity.Post;
import com.seniors.domain.post.repository.PostRepository;
import com.seniors.domain.users.entity.Users;
import com.seniors.domain.users.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;

import static com.seniors.domain.comment.dto.CommentDto.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("dev")
@DirtiesContext
public class CommentControllerTest {

	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UsersRepository usersRepository;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private CommentRepository commentRepository;

	private static Authentication authentication;
	private static Users users;

	@BeforeEach
	void clean() {
		commentRepository.deleteAll();
		postRepository.deleteAll();
		usersRepository.deleteAll();
		users = usersRepository.save(Users.builder()
				// Duplicate 에러로 인해 임시로 timestamp 값으로 해결
				.snsId(String.valueOf(new Timestamp(System.currentTimeMillis()).getTime()))
				.email("test@test.com")
				.gender("male")
				.ageRange("20~29")
				.birthday("12-31")
				.oAuthProvider(OAuthProvider.KAKAO)
				.build());
		CustomUserDetails userDetails = new CustomUserDetails(
				users.getId(),
				users.getSnsId(),
				users.getEmail(),
				users.getNickname(),
				users.getGender(),
				users.getProfileImageUrl());
		userDetails.setUserId(users.getId());

		// Set the Authentication object
		authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	@Test
	@DisplayName("댓글 생성")
	void commentAdd() throws Exception {
		// given
		PostCreateDto postCreateDto = PostCreateDto.builder()
				.title("글 제목입니다.")
				.content("글 내용입니다.")
				.build();
		Post post = postRepository.save(Post.of("글 제목1", "글 내용1", users));
		SaveCommentDto saveCommentDto = SaveCommentDto
				.builder()
				.content("댓글 내용1")
				.postId(post1.getId())
				.build();
		String json = objectMapper.writeValueAsString(saveCommentDto);

		// expected
		mockMvc.perform(post("/api/comments")
						.contentType(APPLICATION_JSON)
						.content(json)
						.principal(authentication)
				)
				.andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	@DisplayName("생성 요청 시 content 값은 필수")
	void commentAddNotExistContent() throws Exception {
		// given


		// expected
	}

	@Test
	@DisplayName("댓글 수정")
	void commentModify() throws Exception {
		// given
		// expected
	}

	@Test
	@DisplayName("댓글 삭제")
	void commentRemove() throws Exception {
		// given
		// expected
	}


}
