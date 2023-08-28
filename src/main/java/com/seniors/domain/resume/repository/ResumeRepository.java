package com.seniors.domain.resume.repository;

import com.seniors.domain.resume.entity.Resume;
import jakarta.persistence.LockModeType;
import org.checkerframework.checker.nullness.Opt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long>, ResumeRepositoryCustom {

    Optional<Resume> findByUsersId(Long userId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Resume r set r.viewCount = r.viewCount + 1 where r.id = :resumeId")
    void increaseViewCount(@Param("resumeId") Long resumeId);

}
