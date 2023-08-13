package com.seniors.domain.resume.entity;

import com.seniors.domain.common.BaseEntity;
import com.seniors.domain.common.BaseTimeEntity;
import com.seniors.domain.resume.dto.CareerDto;
import com.seniors.domain.resume.dto.ResumeDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.Year;

import static java.lang.Boolean.FALSE;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "isDeleted = false")
@SQLDelete(sql = "UPDATE career SET isDeleted = true WHERE id = ?")
public class Career extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "int not null COMMENT '입사 연도'")
    private Integer startedAt;

    @Column(columnDefinition = "int COMMENT '퇴사 연도'")
    private Integer endedAt;

    @Column(columnDefinition = "varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci not null COMMENT '회사'")
    private String company;

    @Column(columnDefinition = "varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci not null COMMENT '직함'")
    private String title;

    @Column
    private Boolean isAttendanced;

    @Column(columnDefinition = "text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci not null COMMENT '내용'")
    private String content;

    @Column
    private Boolean isDeleted = FALSE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resumeId")
    private Resume resume;

    @Builder
    public Career(Integer startedAt, Integer endedAt, String company, String title, Boolean isAttendanced, String content, Resume resume) {
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.company = company;
        this.title = title;
        this.isAttendanced = isAttendanced;
        this.content = content;
        this.resume = resume;
    }

    public static Career from(CareerDto.saveCareerReq saveCareerReq){
        return Career.builder()
                .startedAt(saveCareerReq.getStartedAt())
                .endedAt(saveCareerReq.getEndedAt())
                .company(saveCareerReq.getCompany())
                .title(saveCareerReq.getTitle())
                .isAttendanced(saveCareerReq.getIsAttendanced())
                .content(saveCareerReq.getContent())
                .build();
    }

    public static Career from(CareerDto.modifyCareerReq modifyCareerReq){
        return Career.builder()
                .startedAt(modifyCareerReq.getStartedAt())
                .endedAt(modifyCareerReq.getEndedAt())
                .company(modifyCareerReq.getCompany())
                .title(modifyCareerReq.getTitle())
                .isAttendanced(modifyCareerReq.getIsAttendanced())
                .content(modifyCareerReq.getContent())
                .build();
    }

}
