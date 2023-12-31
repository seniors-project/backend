package com.seniors.domain.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seniors.common.constant.OAuthProvider;
import com.seniors.common.constant.ResultCode;
import com.seniors.config.security.CustomUserDetails;
import com.seniors.config.security.JwtUtil;
import com.seniors.config.security.TokenService;
import com.seniors.domain.post.dto.PostDto.PostCreateDto;
import com.seniors.domain.post.dto.PostDto.SetPostDto;
import com.seniors.domain.post.entity.Post;
import com.seniors.domain.post.entity.PostLike;
import com.seniors.domain.post.repository.post.PostRepository;
import com.seniors.domain.users.entity.Users;
import com.seniors.domain.users.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Slf4j
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("dev")
@Transactional
class PostControllerTest {


	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private PostRepository postRepository;
	@Autowired
	private UsersRepository usersRepository;
	private Authentication authentication;
	private String accessToken;

	@Autowired
	private TokenService tokenService;

	private Users users;

	@BeforeEach
	void setUp() {
		postRepository.deleteAll();
		usersRepository.deleteAll();

		// Random 객체 생성
		Random random = new Random();

		// 10자리 랜덤 숫자 생성
		long randomNumber = random.nextLong() % 10000000000L; // 10자리 이하의 숫자로 제한
		if (randomNumber < 0)
			randomNumber *= -1; // 음수인 경우 양수로 변환

		users = usersRepository.save(Users.builder()
				.snsId(String.valueOf(randomNumber))
				.nickname("tester1")
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

		accessToken = tokenService.generateToken(users, Long.valueOf(1000 * 60 * 60 * 24));

		authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	@Test
	@DisplayName("게시글 생성")
	void postAdd() throws Exception {
		// given
		String title = "글 제목입니다.";
		String content = "글 내용입니다.";
		PostCreateDto createDto = PostCreateDto.builder().title(title).content(content).build();
		ObjectMapper objectMapper = new ObjectMapper();
		String createDtoJson = objectMapper.writeValueAsString(createDto);

//		MockMultipartFile file = new MockMultipartFile("files", "tooth.png", "multipart/form-data", "uploadFile".getBytes(StandardCharsets.UTF_8));
		MockMultipartFile request = new MockMultipartFile("data", null, "application/json", createDtoJson.getBytes(StandardCharsets.UTF_8));

		// expected
		mockMvc.perform(multipart(HttpMethod.POST, "/api/posts")
//						.file(file)
						.file(request)
						.accept(APPLICATION_JSON)
						.contentType(MULTIPART_FORM_DATA)
						.header("Authorization", accessToken)
				)
				.andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	@DisplayName("생성 요청 시 title 값은 필수")
	void postAddNotExistTitle() throws Exception {
		// given
		SetPostDto setPostDto = PostCreateDto.of("글 내용입니다.", "");
		String json = objectMapper.writeValueAsString(setPostDto);
		MockMultipartFile request = new MockMultipartFile("data", null, "application/json", json.getBytes(StandardCharsets.UTF_8));
		// expected
		mockMvc.perform(multipart(HttpMethod.POST, "/api/posts")
								.file(request)
								.accept(APPLICATION_JSON)
								.contentType(MULTIPART_FORM_DATA)
								.header("Authorization", accessToken)
				)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.success").value(false))
				.andExpect(jsonPath("$.code").value(ResultCode.BAD_REQUEST.getCode()))
				.andDo(print());
	}

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

	@Test
	@DisplayName("사용자 글 리스트 조회")
	void getListTest() throws Exception {
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

	@Test
	@DisplayName("게시글 수정")
	void modifyPost() throws Exception {
		// given
		Post post = postRepository.save(Post.of("글 제목1", "글 내용1", users));

		String title = "글 수정 제목1입니다.";
		String content = "글 수정 내용1입니다.";
		SetPostDto setPostDto = PostCreateDto.of(title, content);
		String json = objectMapper.writeValueAsString(setPostDto);
		MockMultipartFile request = new MockMultipartFile("data", null, "application/json", json.getBytes(StandardCharsets.UTF_8));

		// expected
		mockMvc.perform(multipart(HttpMethod.PATCH, "/api/posts/{postId}", post.getId())
						.file(request)
						.accept(APPLICATION_JSON)
						.contentType(MULTIPART_FORM_DATA)
						.header("Authorization", accessToken)
				)
				.andExpect(status().isOk())
				.andDo(print());

	}

	@Test
	@DisplayName("게시글 좋아요")
	void likePost() throws Exception {
		// given
		Post post = postRepository.save(Post.of("글 제목1", "글 내용1", users));

		PostLike postLike = PostLike.of(false, post, users);

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

	@Test
	@DisplayName("게시글 좋아요 실패")
	void failedLikePost() throws Exception {
		// given
		Post post = postRepository.save(Post.of("글 제목1", "글 내용1", users));

		PostLike postLike = PostLike.of(true, post, users);

		String json = objectMapper.writeValueAsString(postLike);
		// expected
		mockMvc.perform(post("/api/posts/like?postId={postId}", post.getId())
						.contentType(APPLICATION_JSON)
						.content(json)
						.principal(authentication)
				)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.success").value(false))
				.andExpect(jsonPath("$.code").value(ResultCode.BAD_REQUEST.getCode()))
				.andDo(print());
	}

}