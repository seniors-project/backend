package com.seniors.domain.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seniors.common.constant.OAuthProvider;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

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

	@BeforeEach
	void clean() {
		commentRepository.deleteAll();
	}

	@Test
	@DisplayName("댓글 생성")
	void commentAdd() throws Exception {
		// given
		Users users = Users.builder()
				.email("test@test.com")
				.nickname("tester")
				.oAuthProvider(OAuthProvider.KAKAO)
				.snsId("alskdkadknasjlfn kakao")
				.build();
		Users users1 = usersRepository.save(users);

		PostCreateDto postCreateDto = PostCreateDto.builder()
				.title("글 제목입니다.")
				.content("글 내용입니다.")
				.userId(users1.getId())
				.build();
		Post post1 = postRepository.save(Post.of(postCreateDto.getTitle(), postCreateDto.getContent()));
		log.info("{}, {}", users1.getId(), post1.getId());
		SaveCommentDto saveCommentDto = SaveCommentDto
				.builder()
				.content("댓글 내용1")
				.postId(post1.getId())
				.userId(users1.getId())
				.build();
		String json = objectMapper.writeValueAsString(saveCommentDto);

		// expected
		mockMvc.perform(post("/api/comments")
						.contentType(APPLICATION_JSON)
						.content(json)
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
