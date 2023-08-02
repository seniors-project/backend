package com.seniors.domain.post.repository.post;

import com.seniors.domain.post.dto.PostDto.GetPostRes;
import com.seniors.domain.post.dto.PostDto.ModifyPostReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

	GetPostRes findOnePost(Long postId);
	void modifyPost(String title, String content, Long postId, Long userId);

	Page<GetPostRes> findAllPost(Pageable pageable);

	void removePost(Long postId, Long userId);

	void increaseLikeCount(Long postId, Boolean status);

}
