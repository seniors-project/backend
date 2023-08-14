package com.seniors.domain.resume.entity;

import com.seniors.domain.resume.dto.ResumeDto;
import com.seniors.domain.users.entity.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResumeView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resumeId")
    private Resume resume;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private Users users;

    @Builder
    public ResumeView(Resume resume, Users users) {
        this.resume = resume;
        this.users = users;
    }

    public static ResumeView of(Resume resume, Users users) {
        return ResumeView.builder()
                .resume(resume)
                .users(users)
                .build();
    }
}
