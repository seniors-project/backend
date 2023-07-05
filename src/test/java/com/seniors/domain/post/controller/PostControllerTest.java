package com.seniors.domain.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seniors.common.constant.ResultCode;
import com.seniors.domain.post.dto.PostDto.PostCreateDto;
import com.seniors.domain.post.entity.Post;
import com.seniors.domain.post.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

	// 다른 테스트에 영향이 가지 않도록 사전에 deleteAll
	@BeforeEach
	void clean() {
		postRepository.deleteAll();
	}

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
				)
				.andExpect(status().isOk())
				.andDo(print());
	}

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
		Post addPost = Post.builder()
				.title("글 제목입니다.")
				.content("글 내용입니다.")
				.isDeleted(false)
				.viewCount(0)
				.likeCount(0)
				.build();
		Post post = postRepository.save(addPost);
		log.info("{}", post.getId());

		// expected
		mockMvc.perform(get("/api/posts/{postId}", post.getId())
						.contentType(APPLICATION_JSON)
				)
				.andExpect(status().isOk())
				.andDo(print());

	}

}