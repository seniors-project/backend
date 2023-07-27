package com.seniors.domain.resume.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import com.seniors.domain.resume.entity.Certificate;
import com.seniors.domain.resume.entity.Resume;
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
    public static class saveCertificateReq extends Certificate {

        @NotEmpty(message = "자격증 명은 비워둘 수 없습니다.")
        @Size(max = 30, message = "자격증 명은 30자 이하여야 합니다.")
        private String name;

        private String rating;

        @NotNull(message = "발행 연도는 비워둘 수 없습니다.")
        private int issuedYear;

        @NotNull(message = "발행 월은 비워둘 수 없습니다.")
        private int issuedMonth;

        private Boolean isIssued;

    }

    @Data
    @Setter
    public static class modifyCertificateReq extends Certificate {

        private Long id;
        @NotEmpty(message = "자격증 명은 비워둘 수 없습니다.")
        @Size(max = 30, message = "자격증 명은 30자 이하여야 합니다.")
        private String name;

        private String rating;

        @NotNull(message = "발행 연도는 비워둘 수 없습니다.")
        private int issuedYear;

        @NotNull(message = "발행 월은 비워둘 수 없습니다.")
        private int issuedMonth;

        private Boolean isIssued;

    }

    @Data
    public static class getCertificateRes {

        private Long id;

        private String name;

        private String rating;

        private int issuedYear;

        private int issuedMonth;

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

        private Long id;

        private String name;

        private String rating;

        private int issuedYear;

        private int issuedMonth;

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