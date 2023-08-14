package com.seniors.domain.post.service;

import com.seniors.common.dto.CustomPage;
import com.seniors.common.exception.type.BadRequestException;
import com.seniors.common.exception.type.NotFoundException;
import com.seniors.config.S3Uploader;
import com.seniors.domain.notification.service.NotificationService;
import com.seniors.domain.post.dto.PostDto.GetPostRes;
import com.seniors.domain.post.dto.PostDto.PostCreateDto;
import com.seniors.domain.post.entity.Post;
import com.seniors.domain.post.entity.PostMedia;
import com.seniors.domain.post.repository.post.PostRepository;
import com.seniors.domain.post.repository.postLike.PostLikeRepository;
import com.seniors.domain.post.repository.postMedia.PostMediaRepository;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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
	private final NotificationService notificationService;

	@Transactional
	public Long addPost(PostCreateDto postCreateDto, BindingResult bindingResult, Long userId) throws IOException {
		if (bindingResult != null && bindingResult.hasErrors()) {
			List<ObjectError> errors = bindingResult.getAllErrors();
			List<String> errorMessages = new ArrayList<>();

			for (ObjectError error : errors) {
				FieldError fieldError = (FieldError) error;
				String message = fieldError.getDefaultMessage();
				errorMessages.add(message);
			}
			throw new BadRequestException(String.join(", ", errorMessages));
		}

		Users users = usersRepository.findById(userId).orElseThrow(
				() -> new NotFoundException("유효하지 않은 회원입니다.")
		);
		Post post = postRepository.save(Post.of(postCreateDto.getTitle(), postCreateDto.getContent(), users));
		if (postCreateDto.getFiles() != null && !postCreateDto.getFiles().isEmpty()) {
			for (MultipartFile file : postCreateDto.getFiles()) {
				String uploadImagePath = s3Uploader.upload(file, "posts/media/" + post.getId().toString());
				postMediaRepository.save(PostMedia.of(uploadImagePath, post));
			}
		}
		return post.getId();
	}

	public GetPostRes findOnePost(Long postId, Long userId) {
		return postRepository.findOnePost(postId, userId);
	}

	public CustomPage<GetPostRes> findPost(int page, int size, Long userId) {
		Direction direction = Direction.DESC;
		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "id"));
		Page<GetPostRes> posts = postRepository.findAllPost(pageable, userId);
		return CustomPage.of(posts);
	}

	@Transactional
	public Long modifyPost(PostCreateDto postCreateDto, BindingResult bindingResult, Long postId, Long userId) throws IOException {

		if (bindingResult != null && bindingResult.hasErrors()) {
			List<ObjectError> errors = bindingResult.getAllErrors();
			List<String> errorMessages = new ArrayList<>();

			for (ObjectError error : errors) {
				FieldError fieldError = (FieldError) error;
				String message = fieldError.getDefaultMessage();
				errorMessages.add(message);
			}
			throw new BadRequestException(String.join(", ", errorMessages));
		}
		Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("유효하지 않은 게시글입니다."));
		postRepository.modifyPost(postCreateDto.getTitle(), postCreateDto.getContent(), postId, userId);

		// 기존 미디어 파일 삭제
		s3Uploader.deleteS3Object("posts/media/" + post.getId().toString());

		postMediaRepository.deleteByPostId(postId);

		if (postCreateDto.getFiles() != null && !postCreateDto.getFiles().isEmpty()) {
			for (MultipartFile file : postCreateDto.getFiles()) {
				String uploadImagePath = s3Uploader.upload(file, "posts/media/" + post.getId().toString());
				postMediaRepository.save(PostMedia.of(uploadImagePath, post));
			}
		}
		return post.getId();
	}

	@Transactional
	public void removePost(Long postId, Long userId) {
		postRepository.removePost(postId, userId);
	}

	@Transactional
	public void likePost(Long postId, Long userId, Boolean status) {
		Boolean likeStatus = postLikeRepository.findStatusByPostIdAndUserId(postId, userId);
		if (status && likeStatus == null)
			throw new BadRequestException("잘못된 좋아요 요청입니다.");

		if (status == likeStatus || likeStatus == null) {
			int updatedRows = postLikeRepository.likePost(postId, userId, !status);
			if (updatedRows >= 1) {
				postRepository.increaseLikeCount(postId, status);
				Post post = postRepository.findById(postId).orElseThrow(
						() -> new NotFoundException("존재하지 않은 게시글입니다.")
				);
				Users users = usersRepository.findById(userId).orElseThrow(
						() -> new NotFoundException("유효하지 않은 회원입니다.")
				);
				if (!post.getUsers().getId().equals(users.getId()) && !status) {
					notificationService.send(post.getUsers(), post, "누군가가 내 피드에 좋아요를 눌렀습니다.");
				}
			} else {
				throw new BadRequestException("좋아요 반영이 실패되었습니다.");
			}
		} else {
			throw new BadRequestException("잘못된 좋아요 요청입니다.");
		}

	}

	@Transactional
	public void postMediaAdd(String uploadImagePath, Long postId) {
		postRepository.findById(postId).ifPresent(posts ->
				postMediaRepository.save(PostMedia.of(uploadImagePath, posts))
		);
	}
}
