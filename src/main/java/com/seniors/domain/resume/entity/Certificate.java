package com.seniors.domain.resume.entity;

import com.seniors.domain.common.BaseEntity;
import com.seniors.domain.common.BaseTimeEntity;
import com.seniors.domain.resume.dto.CertificateDto;
import com.seniors.domain.resume.dto.ResumeDto;
import com.seniors.domain.users.entity.Users;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import static java.lang.Boolean.FALSE;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "isDeleted = false")
@SQLDelete(sql = "UPDATE certificate SET isDeleted = true WHERE id = ?")
public class Certificate extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci not null COMMENT '이름'")
    private String name;

    @Column(columnDefinition = "varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci not null COMMENT '점수/등급'")
    private String rating;

    @Column(columnDefinition = "int not null COMMENT '발행 연도'")
    private Integer issuedYear;

    @Column(columnDefinition = "int not null COMMENT '발행 월'")
    private Integer issuedMonth;

    @Column
    private Boolean isIssued;

    @Column
    private Boolean isDeleted = FALSE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resumeId")
    private Resume resume;

    @Builder
    public Certificate(String name, String rating, int issuedYear, int issuedMonth, Boolean isIssued, Resume resume) {
        this.name = name;
        this.rating = rating;
        this.issuedYear = issuedYear;
        this.issuedMonth = issuedMonth;
        this.isIssued = isIssued;
        this.resume = resume;
    }

    public static Certificate from(CertificateDto.saveCertificateReq saveCertificateReq){
        return Certificate.builder()
                .name(saveCertificateReq.getName())
                .rating(saveCertificateReq.getRating())
                .issuedYear(saveCertificateReq.getIssuedYear())
                .issuedMonth(saveCertificateReq.getIssuedMonth())
                .isIssued(saveCertificateReq.getIsIssued())
                .build();
    }

    public static Certificate from(CertificateDto.modifyCertificateReq modifyCertificateReq){
        return Certificate.builder()
                .name(modifyCertificateReq.getName())
                .rating(modifyCertificateReq.getRating())
                .issuedYear(modifyCertificateReq.getIssuedYear())
                .issuedMonth(modifyCertificateReq.getIssuedMonth())
                .isIssued(modifyCertificateReq.getIsIssued())
                .build();
    }
}
