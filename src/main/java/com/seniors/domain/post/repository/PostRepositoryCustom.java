package com.seniors.domain.post.repository;

import com.seniors.domain.post.dto.PostDto;
import com.seniors.domain.post.dto.PostDto.GetPostRes;

public interface PostRepositoryCustom {

	GetPostRes getOnePost(Long postId, Long userId);
}
