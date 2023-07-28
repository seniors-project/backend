package com.seniors.domain.resume.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import com.seniors.domain.resume.entity.Career;
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
public class CareerDto {

    @Data
    @Setter
    public static class saveCareerReq {


        @NotNull(message = "입사연도는 비워둘 수 없습니다.")
        @Schema(description = "입사 연도")
        private int startedAt;

        @Schema(description = "퇴사 연도")
        @NotNull(message = "퇴사연도는 비워둘 수 없습니다.")
        private int endedAt;

        @Schema(description = "회사")
        @NotEmpty(message = "회사명은 비워둘 수 없습니다.")
        @Size(max = 30, message = "회사명은 30자 이하여야 합니다.")
        private String company;

        @Schema(description = "직함")
        @NotEmpty(message = "직함은 비워둘 수 없습니다.")
        @Size(max = 30, message = "직함은 30자 이하여야 합니다.")
        private String title;

        @Schema(description = "재직 여부")
        private Boolean isAttendanced;

        @NotEmpty(message = "내용은 비워둘 수 없습니다.")
        @Schema(description = "내용")
        private String content;


    }

    @Data
    @Setter
    public static class modifyCareerReq {

        @Schema(description = "고유키")
        private Long id;

        @Schema(description = "입사 연도")
        @NotNull(message = "입사연도는 비워둘 수 없습니다.")
        private int startedAt;

        @Schema(description = "퇴사 연도")
        @NotNull(message = "퇴사연도는 비워둘 수 없습니다.")
        private int endedAt;

        @Schema(description = "회사")
        @NotEmpty(message = "회사명은 비워둘 수 없습니다.")
        @Size(max = 30, message = "회사명은 30자 이하여야 합니다.")
        private String company;

        @Schema(description = "직함")
        @NotEmpty(message = "직함은 비워둘 수 없습니다.")
        @Size(max = 30, message = "직함은 30자 이하여야 합니다.")
        private String title;

        @Schema(description = "재직 여부")
        private Boolean isAttendanced;

        @Schema(description = "내용")
        @NotEmpty(message = "내용은 비워둘 수 없습니다.")
        private String content;


    }

    @Data
    public static class getCareerRes {

        @Schema(description = "고유키")
        private Long id;

        @Schema(description = "입사 연도")
        private int startedAt;

        @Schema(description = "퇴사 연도")
        private int endedAt;

        @Schema(description = "회사")
        private String company;

        @Schema(description = "직함")
        private String title;

        @Schema(description = "재직 여부")
        private Boolean isAttendanced;

        @Schema(description = "내용")
        private String content;

        @Builder
        private getCareerRes(Career career){
            this.id = career.getId();
            this.startedAt = career.getStartedAt();
            this.endedAt = career.getEndedAt();
            this.company = career.getCompany();
            this.title = career.getCompany();
            this.isAttendanced = career.getIsAttendanced();
            this.content = career.getContent();
        }

        public static getCareerRes from(Career career) {
            return getCareerRes.builder()
                    .career(career)
                    .build();
        }
    }

    @Data
    @NoArgsConstructor
    @Getter
    @Setter
    public static class getCareerQueryDslRes{

        @Schema(description = "고유키")
        private Long id;

        @Schema(description = "입사 연도")
        private int startedAt;

        @Schema(description = "퇴사 연도")
        private int endedAt;

        @Schema(description = "회사")
        private String company;

        @Schema(description = "직함")
        private String title;

        @Schema(description = "재직 여부")
        private Boolean isAttendanced;

        @Schema(description = "내용")
        private String content;

        public getCareerQueryDslRes(Career career){
            this.id = career.getId();
            this.startedAt = career.getStartedAt();
            this.endedAt = career.getEndedAt();
            this.company = career.getCompany();
            this.title = career.getTitle();
            this.isAttendanced = career.getIsAttendanced();
            this.content = career.getContent();
        }
    }
}
