package com.seniors.domain.post.repository;

import com.seniors.domain.post.dto.PostDto.GetPostRes;
import com.seniors.domain.post.dto.PostDto.ModifyPostReq;
import com.seniors.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

	GetPostRes findOnePost(Long postId, Long userId);
	void modifyPost(ModifyPostReq modifyPostReq, Long postId, Long userId);

	Page<GetPostRes> findAllPost(Pageable pageable);
}
