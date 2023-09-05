package com.seniors.domain.resume.entity;

import com.seniors.domain.comment.entity.Comment;
import com.seniors.domain.common.BaseEntity;
import com.seniors.domain.common.BaseTimeEntity;
import com.seniors.domain.post.entity.Post;
import com.seniors.domain.resume.dto.ResumeDto;
import com.seniors.domain.users.entity.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.type.TrueFalseConverter;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.FALSE;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "isDeleted = false")
@SQLDelete(sql = "UPDATE resume SET isDeleted = true WHERE id = ?")
public class Resume extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  null COMMENT '소개글'")
    private String introduce;

    @Column(columnDefinition = "text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '사진 URL'")
    private String photoUrl;

    @Column(columnDefinition = "varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci not null COMMENT '직종'")
    private String occupation;

    @Column
    private Boolean isDeleted = FALSE;

    @Column
    private Boolean isOpened;

    @Column(columnDefinition = "varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci not null COMMENT '이름'")
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private Users users;

    @Column
    private Integer viewCount = 0;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "resume", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Certificate> certificates = new ArrayList<>();


    @BatchSize(size = 100)
    @OneToMany(mappedBy = "resume", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Career> careers = new ArrayList<>();

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "resume", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Education> educations = new ArrayList<>();

    @Builder
    public Resume(String introduce, String photo_url, String occupation, Boolean isOpened, String name, Users users) {
        this.introduce = introduce;
        this.photoUrl = photo_url;
        this.occupation = occupation;
        this.isOpened = isOpened;
        this.name = name;
        this.users = users;
    }

    public static Resume of(ResumeDto.SaveResumeReq resumeReq, Users users) {
        return Resume.builder()
                .introduce(resumeReq.getIntroduce())
                .occupation(resumeReq.getOccupation())
                .isOpened(resumeReq.getIsOpened())
                .name(resumeReq.getName())
                .users(users)
                .build();
    }

    public void addCareer(Career career) {
        this.getCareers().add(career);
        career.setResume(this);
    }

    public void addCertificate(Certificate certificate) {
        this.getCertificates().add(certificate);
        certificate.setResume(this);
    }

    public void addEducation(Education education) {
        this.getEducations().add(education);
        education.setResume(this);
    }

    public void uploadPhotoUrl(String photoUrl){
        this.photoUrl = photoUrl;
    }

    public void update(ResumeDto.ModifyResumeReq modifyResumeReq, String photoUrl){
        this.introduce = modifyResumeReq.getIntroduce();
        this.photoUrl = photoUrl;
        this.occupation = modifyResumeReq.getOccupation();
        this.isOpened = modifyResumeReq.getIsOpened();
        this.name = modifyResumeReq.getName();
    }

    public void increaseViewCount() {
        this.viewCount += 1;
    }
}
