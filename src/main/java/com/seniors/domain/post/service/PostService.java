package com.seniors.domain.post.service;

import com.seniors.common.dto.CustomPage;
import com.seniors.common.exception.type.BadRequestException;
import com.seniors.common.exception.type.NotFoundException;
import com.seniors.config.S3Uploader;
import com.seniors.domain.post.dto.PostDto.GetPostRes;
import com.seniors.domain.post.dto.PostDto.ModifyPostReq;
import com.seniors.domain.post.dto.PostDto.SavePostReq;
import com.seniors.domain.post.entity.Post;
import com.seniors.domain.post.entity.PostMedia;
import com.seniors.domain.post.repository.postMedia.PostMediaRepository;
import com.seniors.domain.post.repository.postLike.PostLikeRepository;
import com.seniors.domain.post.repository.post.PostRepository;
import com.seniors.domain.users.entity.Users;
import com.seniors.domain.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepository;
	private final PostMediaRepository postMediaRepository;
	private final PostLikeRepository postLikeRepository;
	private final UsersRepository usersRepository;
	private final S3Uploader s3Uploader;

	@Transactional
	public void addPost(String title, String content, List<MultipartFile> files, Long userId) throws IOException {
		if (title == null || title.isEmpty() || content == null || content.isEmpty()) {
			throw new BadRequestException("Title or Content is required");
		}

		Users users = usersRepository.findById(userId).orElseThrow(
				() -> new NotFoundException("유효하지 않은 회원입니다.")
		);
		Post post = postRepository.save(Post.of(title, content, users));
		if (files != null && !files.isEmpty()) {
			for (MultipartFile file : files) {
				String uploadImagePath = s3Uploader.upload(file, "posts/media/" + post.getId().toString());
				postMediaRepository.save(PostMedia.of(uploadImagePath, post));
			}
		}
	}

	@Transactional
	public GetPostRes findOnePost(Long postId) {
		return postRepository.findOnePost(postId);
	}

	@Transactional(readOnly = true)
	public CustomPage<GetPostRes> findPost(int page, int size) {
		Direction direction = Direction.DESC;
		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "id"));
		Page<GetPostRes> posts = postRepository.findAllPost(pageable);
		return CustomPage.of(posts);
	}

	@Transactional
	public void modifyPost(String title, String content, List<MultipartFile> files, Long postId, Long userId) throws IOException {
		Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("유효하지 않은 게시글입니다."));
		postRepository.modifyPost(title, content, postId, userId);
		postMediaRepository.deleteByPostId(postId);

		if (files != null && !files.isEmpty()) {
			for (MultipartFile file : files) {
				String uploadImagePath = s3Uploader.upload(file, "posts/media/" + post.getId().toString());
				postMediaRepository.save(PostMedia.of(uploadImagePath, post));
			}
		}
	}

	@Transactional
	public void removePost(Long postId, Long userId) {
		postRepository.removePost(postId, userId);
	}

	@Transactional
	public void likePost(Long postId, Long userId, Boolean status) {
		int updatedRows = postLikeRepository.likePost(postId, userId, !status);
		if (updatedRows >= 1) {
			postRepository.increaseLikeCount(postId, status);
		} else {
			throw new BadRequestException();
		}
	}

	@Transactional
	public void postMediaAdd(String uploadImagePath, Long postId) {
		postRepository.findById(postId).ifPresent(posts ->
				postMediaRepository.save(PostMedia.of(uploadImagePath, posts))
		);
	}
}
