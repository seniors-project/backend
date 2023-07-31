package com.seniors.domain.resume.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seniors.domain.resume.dto.*;
import com.seniors.domain.resume.entity.QCertificate;
import com.seniors.domain.resume.entity.QResume;
import com.seniors.domain.resume.entity.Resume;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.querydsl.core.group.GroupBy.list;
import static com.seniors.domain.resume.entity.QCareer.career;
import static com.seniors.domain.resume.entity.QCertificate.certificate;
import static com.seniors.domain.resume.entity.QEducation.education;
import static com.seniors.domain.resume.entity.QResume.resume;
import static org.hibernate.query.results.Builders.fetch;

@RequiredArgsConstructor
@Repository
public class ResumeRepositoryImpl implements ResumeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<ResumeDto.GetResumeByQueryDslRes> findResumeList(Pageable pageable, Long lastId, Long userId) {

        List<Resume> results = queryFactory
                .selectFrom(resume)
                .where(
                        userIdEq(userId),
                        ltResumeId(lastId),
                        resume.isOpened.eq(Boolean.TRUE)
                )
                .orderBy(resume.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        List<ResumeDto.GetResumeByQueryDslRes> resultDtos = results.stream()
                .map(ResumeDto.GetResumeByQueryDslRes::new)
                .collect(Collectors.toList());

        return checkLastPage(pageable, resultDtos);
    }
    // no-offset 방식 처리하는 메서드
    private BooleanExpression ltResumeId(Long lastId) {
        if (lastId == null) {
            return null;
        }
        return resume.id.lt(lastId);
    }

    private BooleanExpression userIdEq(Long userId) {
        return resume.users.id.ne(userId);
    }

    // 무한 스크롤 방식 처리하는 메서드
    private Slice<ResumeDto.GetResumeByQueryDslRes> checkLastPage(Pageable pageable, List<ResumeDto.GetResumeByQueryDslRes> resultDtos) {

        boolean hasNext = false;

        // 조회한 결과 개수가 요청한 페이지 사이즈보다 크면 뒤에 더 있음, next = true
        if (resultDtos.size() > pageable.getPageSize()) {
            hasNext = true;
            resultDtos.remove(pageable.getPageSize());
        }
        return new SliceImpl<>(resultDtos, pageable, hasNext);
    }


}
