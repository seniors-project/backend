package com.seniors.domain.post.repository.postMedia;

import com.seniors.domain.post.entity.PostMedia;

import java.util.List;

public interface PostMediaRepositoryCustom {

	void deleteByPostId(Long postId);

	List<PostMedia> findByPostId(Long postId);
}
