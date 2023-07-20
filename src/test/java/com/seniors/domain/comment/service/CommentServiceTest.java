package com.seniors.domain.comment.service;

import com.seniors.domain.comment.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
class CommentServiceTest {

	@Autowired
	private CommentService commentService;

	@Autowired
	private CommentRepository commentRepository;
	@BeforeEach
	void clean() {
		commentRepository.deleteAll();
	}
}
