package com.seniors.domain.post.repository.postMedia;

import com.seniors.domain.post.entity.PostMedia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostMediaRepository extends JpaRepository<PostMedia, Long>, PostMediaRepositoryCustom {
}
