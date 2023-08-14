package com.seniors.domain.post.repository.postLike;

import com.seniors.domain.post.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "insert into PostLike set postId = :postId, userId = :userId, status = :status " +
            "on duplicate key update status = :status", nativeQuery = true)
    int likePost(@Param("postId") Long postId, @Param("userId") Long userId, @Param("status") boolean status);

    @Query(value = "select status from PostLike where postId = :postId and userId = :userId", nativeQuery = true)
    Boolean findStatusByPostIdAndUserId(@Param("postId") Long postId, @Param("userId") Long userId);
}
