package com.seniors.domain.post.service;

import com.seniors.domain.post.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
class PostServiceTest {

	@Autowired
	private PostService postService;
	@Autowired
	private PostRepository postRepository;

	@BeforeEach
	void clean() {
		postRepository.deleteAll();
	}

//	@Test
//	@DisplayName("글 작성")
//	void postTest1() {
//		// given
//		Post savePost = Post.builder()
//				.title("글 제목입니다.")
//				.content("글 내용입니다.")
//				.isDeleted(false)
//				.viewCount(0)
//				.likeCount(0)
//				.build();
//
//		// when
//		postService.addPost(savePost);
//
//		// then
//		assertEquals(1L, postRepository.count());
//		Post post = postRepository.findAll().get(0);
//		assertEquals("글 제목입니다.", post.getTitle());
//		assertEquals("글 내용입니다.", post.getContent());
//	}

//	@Test
//	@DisplayName("글 단건 조회")
//	void getOneTest1() {
//		// given
//		Post savePost = Post.builder()
//				.title("foo")
//				.content("bar")
//				.viewCount(0)
//				.likeCount(0)
//				.isDeleted(false)
//				.build();
//		postRepository.save(savePost);
//
//		// when
//		Post post = postService.findPost(savePost.getId());
//
//		// then
//		assertNotNull(post);
//		assertEquals(1L, postRepository.count());
//		assertEquals("foo", post.getTitle());
//		assertEquals("bar", post.getContent());
//	}

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
