package com.seniors.domain.resume.repository;

import com.seniors.domain.resume.dto.ResumeDto;
import com.seniors.domain.resume.entity.Resume;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumeRepositoryCustom {

    Slice<ResumeDto.GetResumeByQueryDslRes> findResumeList(Pageable pageable, Long lastId, Long userId);
}
