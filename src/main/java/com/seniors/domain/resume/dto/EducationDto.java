package com.seniors.domain.resume.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import com.seniors.domain.resume.entity.Certificate;
import com.seniors.domain.resume.entity.Education;
import com.seniors.domain.resume.entity.Resume;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Year;

import static java.lang.Boolean.FALSE;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Setter
public class EducationDto {

    @Data
    @Setter
    public static class saveEducationReq {

        @NotEmpty(message = "학교/교육기관 명은 비워둘 수 없습니다.")
        @Size(max = 30, message = "학교/교육기관 명은 30자 이하여야 합니다.")
        @Schema(description = "기관")
        private String institution;

        @Schema(description = "전공/과정")
        private String process;

        @NotNull(message = "시작 연도는 비워둘 수 없습니다.")
        @Schema(description = "시작 연도")
        private Integer  startedAt;

        @Schema(description = "종료 연도")
        private Integer endedAt;

        @Schema(description = "내용")
        private String content;

        @Schema(description = "진행 여부")
        private Boolean isProcessed;

    }

    @Data
    @Setter
    public static class modifyEducationReq {

        @Schema(description = "기본키")
        private Long id;
        @NotEmpty(message = "학교/교육기관 명은 비워둘 수 없습니다.")
        @Size(max = 30, message = "학교/교육기관 명은 30자 이하여야 합니다.")
        @Schema(description = "학교/교육기관")
        private String institution;

        @Schema(description = "전공/과정")
        private String process;

        @NotNull(message = "시작 연도는 비워둘 수 없습니다.")
        @Schema(description = "시작 연도")
        private Integer startedAt;

        @NotNull(message = "종료 연도는 비워둘 수 없습니다.")
        @Schema(description = "종료 연도")
        private Integer endedAt;

        @Schema(description = "내용")
        private String content;

        @Schema(description = "진행 여부")
        private Boolean isProcessed;

    }

    @Data
    public static class getEducationRes {

        @Schema(description = "기본키")
        private Long id;

        @Schema(description = "기관")
        private String institution;

        @Schema(description = "전공/과정")
        private String process;

        @Schema(description = "시작 연도")
        private Integer startedAt;

        @Schema(description = "종료 연도")
        private Integer endedAt;

        @Schema(description = "내용")
        private String content;
        @Schema(description = "진행 여부")
        private Boolean isProcessed;

        @Builder
        private getEducationRes(Education education){
            this.id = education.getId();
            this.institution = education.getInstitution();
            this.process = education.getProcess();
            this.startedAt = education.getStartedAt();
            this.endedAt = education.getEndedAt();
            this.content = education.getContent();
            this.isProcessed = education.getIsProcessed();
        }

        public static EducationDto.getEducationRes from(Education education) {
            return getEducationRes.builder()
                    .education(education)
                    .build();
        }

    }

    @Data
    @NoArgsConstructor
    @Getter
    @Setter
    public static class getEducationByQueryDslRes {

        @Schema(description = "기본키")
        private Long id;

        @Schema(description = "기관")
        private String institution;

        @Schema(description = "전공/과정")
        private String process;

        @Schema(description = "시작 연도")
        private Integer startedAt;

        @Schema(description = "종료 연도")
        private Integer endedAt;

        @Schema(description = "내용")
        private String content;

        @Schema(description = "진행 여부")
        private Boolean isProcessed;

        public getEducationByQueryDslRes(Education education) {
            this.id = education.getId();
            this.institution = education.getInstitution();
            this.process = education.getProcess();
            this.startedAt = education.getStartedAt();
            this.endedAt = education.getEndedAt();
            this.content = education.getContent();
            this.isProcessed = education.getIsProcessed();
        }
    }

}
