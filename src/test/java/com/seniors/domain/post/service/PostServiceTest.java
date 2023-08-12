package com.seniors.domain.post.service;

import com.seniors.common.constant.OAuthProvider;
import com.seniors.config.security.CustomUserDetails;
import com.seniors.domain.post.dto.PostDto;
import com.seniors.domain.post.dto.PostDto.GetPostRes;
import com.seniors.domain.post.dto.PostDto.PostCreateDto;
import com.seniors.domain.post.entity.Post;
import com.seniors.domain.post.repository.post.PostRepository;
import com.seniors.domain.users.entity.Users;
import com.seniors.domain.users.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("dev")
class PostServiceTest {

	@Autowired
	private PostService postService;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private UsersRepository usersRepository;
	public BindingResult bindingResult;
	private Users users;

	@BeforeEach
	void clean() {
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
				.email("test@test.com")
				.nickname("tester")
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

	}

	@Test
	@DisplayName("글 작성")
	void postSave() throws IOException {
		// given
		List<MultipartFile> files = new ArrayList<>();

		Post post = Post.builder()
				.title("글 제목입니다.")
				.content("글 내용입니다.")
				.isDeleted(false)
				.likeCount(0)
				.users(users)
				.build();

		// when
		Long postId = postService.addPost(PostCreateDto.of(post.getTitle(), post.getContent(), files), bindingResult, users.getId());

		// then
		assertEquals(1L, postRepository.count());
		Post savePost = postRepository.findById(postId).orElse(null);
		assertEquals("글 제목입니다.", savePost.getTitle());
		assertEquals("글 내용입니다.", savePost.getContent());
	}

	@Test
	@DisplayName("글 단건 조회")
	void getOneTest1() throws IOException {
		// given
		List<MultipartFile> files = new ArrayList<>();

		Post post = Post.builder()
				.title("foo")
				.content("bar")
				.isDeleted(false)
				.likeCount(0)
				.users(users)
				.build();
		Long postId = postService.addPost(PostCreateDto.of(post.getTitle(), post.getContent(), files), bindingResult, users.getId());


		// when
		GetPostRes findPost = postService.findOnePost(postId, users.getId());

		// then
		assertNotNull(post);
		assertEquals(1L, postRepository.count());
		assertEquals("foo", post.getTitle());
		assertEquals("bar", post.getContent());
	}

//	@Test
//	@DisplayName("글 단건 삭제")
//	void deletePostTest1() {
//		// given
//		Post post = Post.builder()
//				.title("test title 1")
//				.content("test content 1")
//				.isDeleted(false)
//				.viewCount(0)
//				.likeCount(0)
//				.build();
//		postRepository.save(post);
//
//		// when
//		postService.removePost(post.getId());
//
//		// then
//		assertEquals(0, postRepository.count());
//	}

}
