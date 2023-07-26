package com.seniors.domain.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seniors.common.constant.OAuthProvider;
import com.seniors.common.constant.ResultCode;
import com.seniors.config.security.CustomUserDetails;
import com.seniors.domain.post.dto.PostDto.PostCreateDto;
import com.seniors.domain.post.dto.PostLikeDto;
import com.seniors.domain.post.entity.Post;
import com.seniors.domain.post.entity.PostLike;
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
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.seniors.domain.post.dto.PostLikeDto.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Slf4j
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("dev")
@DirtiesContext
class PostControllerTest {

	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private PostRepository postRepository;
	@Autowired
	private UsersRepository usersRepository;
	private static Authentication authentication;
	private static Users users;

	// 다른 테스트에 영향이 가지 않도록 사전에 deleteAll
	@BeforeEach
	void clean() {
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

	@Transactional
	@Test
	@DisplayName("게시글 생성")
	void postAdd() throws Exception {
		// given
		PostCreateDto postCreateDto = PostCreateDto.builder()
				.title("글 제목입니다.")
				.content("글 내용입니다.")
				.build();
		String json = objectMapper.writeValueAsString(postCreateDto);     // Javascript의 JSON.stringfy(object)

		// expected
		mockMvc.perform(post("/api/posts")
						.contentType(APPLICATION_JSON)
						.content(json)
						.principal(authentication)
				)
				.andExpect(status().isOk())
				.andDo(print());
	}

	@Transactional
	@Test
	@DisplayName("생성 요청 시 title 값은 필수")
	void postAddNotExistTitle() throws Exception {
		// given
		PostCreateDto request = PostCreateDto.builder()
				.content("글 내용입니다.")
				.build();
		String json = objectMapper.writeValueAsString(request);

		// expected
		mockMvc.perform(post("/api/posts")
						.contentType(APPLICATION_JSON)
						.content(json)
						.principal(authentication)
				)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.success").value(false))
				.andExpect(jsonPath("$.code").value(ResultCode.BAD_REQUEST.getCode()))
				.andDo(print());
	}

	@Transactional
	@Test
	@DisplayName("게시글 단건 조회")
	void findOnePost() throws Exception {
		// given
		Post post = postRepository.save(Post.of("글 제목1", "글 내용1", users));

		// expected
		mockMvc.perform(get("/api/posts/{postId}", post.getId())
						.contentType(APPLICATION_JSON)
						.principal(authentication)
				)
				.andExpect(status().isOk())
				.andDo(print());
	}

	@Transactional
	@Test
	@DisplayName("사용자 글 리스트 조회")
	void getListTest2() throws Exception {
		// given
		List<Post> requestPosts = IntStream.range(0, 5)
				.mapToObj(i -> Post.builder()
						.title("seniors title " + i + 1)
						.content("seniors content " + i + 1)
						.isDeleted(false)
						.likeCount(0)
						.users(users)
						.build())
				.collect(Collectors.toList());
		postRepository.saveAll(requestPosts);

		// expected
		mockMvc.perform(get("/api/posts?page=1&size=5")
						.contentType(APPLICATION_JSON)
						.principal(authentication)
				)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.list.length()", is(5)))
				.andExpect(jsonPath("$.data.list[0].postId").value(requestPosts.get(requestPosts.size() - 1).getId()))
				.andExpect(jsonPath("$.code").value(ResultCode.OK.getCode()))
				.andExpect(jsonPath("$.message").value(ResultCode.OK.getMessage()))
				.andDo(print());

	}

	@Transactional
	@Test
	@DisplayName("게시글 수정")
	void modifyPost() throws Exception {
		// given
		Post post = postRepository.save(Post.of("글 제목1", "글 내용1", users));

		PostCreateDto modifyPostReq = PostCreateDto.builder()
				.title("변경 제목1")
				.content("변경 내용1")
				.build();
		String json = objectMapper.writeValueAsString(modifyPostReq);     // Javascript의 JSON.stringfy(object)

		// expected
		mockMvc.perform(patch("/api/posts/{postId}", post.getId())
						.contentType(APPLICATION_JSON)
						.content(json)
						.principal(authentication)
				)
				.andExpect(status().isOk())
				.andDo(print());

	}

	@Transactional
	@Test
	@DisplayName("게시글 좋아요")
	void likePost() throws Exception {
		// given
		Post post = postRepository.save(Post.of("글 제목1", "글 내용1", users));

		PostLike postLike = PostLike.from(post.getId(), users.getId(), 0);

		String json = objectMapper.writeValueAsString(postLike);
		// expected
		mockMvc.perform(post("/api/posts/like?postId={postId}", post.getId())
						.contentType(APPLICATION_JSON)
						.content(json)
						.principal(authentication)
				)
				.andExpect(status().isOk())
				.andDo(print());
	}


}