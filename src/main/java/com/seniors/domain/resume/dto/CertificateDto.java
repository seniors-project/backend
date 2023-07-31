package com.seniors.domain.resume.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import com.seniors.domain.resume.entity.Certificate;
import com.seniors.domain.resume.entity.Resume;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import static java.lang.Boolean.FALSE;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Setter
public class CertificateDto {

    @Data
    @Setter
    public static class saveCertificateReq{

        @NotEmpty(message = "자격증 명은 비워둘 수 없습니다.")
        @Size(max = 30, message = "자격증 명은 30자 이하여야 합니다.")
        @Schema(description = "이름")
        private String name;

        @Schema(description = "점수/등급")
        private String rating;

        @Schema(description = "발행 연도")
        @NotNull(message = "발행 연도는 비워둘 수 없습니다.")
        private int issuedYear;

        @Schema(description = "발행 월")
        @NotNull(message = "발행 월은 비워둘 수 없습니다.")
        private int issuedMonth;

        @Schema(description = "발행 여부")
        private Boolean isIssued;

    }

    @Data
    @Setter
    public static class modifyCertificateReq {

        @Schema(description = "고유키")
        private Long id;
        @NotEmpty(message = "자격증 명은 비워둘 수 없습니다.")
        @Size(max = 30, message = "자격증 명은 30자 이하여야 합니다.")
        @Schema(description = "이름")
        private String name;

        @Schema(description = "점수/등급")
        private String rating;

        @Schema(description = "발행 연도")
        @NotNull(message = "발행 연도는 비워둘 수 없습니다.")
        private int issuedYear;

        @Schema(description = "발행 월")
        @NotNull(message = "발행 월은 비워둘 수 없습니다.")
        private int issuedMonth;

        @Schema(description = "발행 여부")
        private Boolean isIssued;

    }

    @Data
    public static class getCertificateRes {

        @Schema(description = "고유키")
        private Long id;

        @Schema(description = "이름")
        private String name;

        @Schema(description = "점수/등급")
        private String rating;

        @Schema(description = "발행 연도")
        private int issuedYear;

        @Schema(description = "발행 월")
        private int issuedMonth;

        @Schema(description = "발행 여부")
        private Boolean isIssued;

        @Builder
        private getCertificateRes(Certificate certificate) {
            this.id = certificate.getId();
            this.name = certificate.getName();
            this.rating = certificate.getRating();
            this.issuedYear = certificate.getIssuedYear();
            this.issuedMonth = certificate.getIssuedMonth();
            this.isIssued = certificate.getIsIssued();
        }

        public static getCertificateRes from(Certificate certificate) {
            return getCertificateRes.builder()
                    .certificate(certificate)
                    .build();
        }
    }

    @Data
    @NoArgsConstructor
    @Getter
    @Setter
    public static class getCertificateQueryDslRes {

        @Schema(description = "고유키")
        private Long id;

        @Schema(description = "이름")
        private String name;

        @Schema(description = "점수/등급")
        private String rating;

        @Schema(description = "발행 연도")
        private int issuedYear;

        @Schema(description = "발행 월")
        private int issuedMonth;

        @Schema(description = "발행 여부")
        private Boolean isIssued;

        public getCertificateQueryDslRes(Certificate certificate) {
            this.id = certificate.getId();
            this.name = certificate.getName();
            this.rating = certificate.getRating();
            this.issuedYear = certificate.getIssuedYear();
            this.issuedMonth = certificate.getIssuedMonth();
            this.isIssued = certificate.getIsIssued();
        }

    }
}