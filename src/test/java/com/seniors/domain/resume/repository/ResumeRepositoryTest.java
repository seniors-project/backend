package com.seniors.domain.resume.repository;

import com.seniors.domain.resume.entity.Career;
import com.seniors.domain.resume.entity.Certificate;
import com.seniors.domain.resume.entity.Education;
import com.seniors.domain.resume.entity.Resume;
import com.seniors.domain.users.entity.Users;
import com.seniors.domain.users.repository.UsersRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.print.Book;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.in;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@ActiveProfiles("dev")
public class ResumeRepositoryTest {
    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Test
    @DisplayName("이력서를 등록한다.")
    void addResume() {
        // given
        Users users = createUser("mike", "male", "08-08", "20~29", "https:~");
        Users savedUser = usersRepository.save(users);

        Resume resume = createResume("안녕하세요~!!", "https:~", "개발자", true, "박철수", savedUser);

        Certificate certificate = createCertificate("OPIC", "AL", 2015, 8, true);
        resume.addCertificate(certificate);

        Career career = createCareer(1998, 2007, "Naver", "CTO", false, "정산 업무 담당");
        resume.addCareer(career);

        Education education = createEducation("ABC 대학교", "컴퓨터 공학과", 1980, 1984, "컴퓨터 공학과 전공했습니다.", false);
        resume.addEducation(education);

        // when
        Resume savedResume = resumeRepository.save(resume);

        // then
        assertThat(resume).isEqualTo(savedResume);
        assertThat(resumeRepository.count()).isEqualTo(1);
        assertThat(savedResume.getEducations().size()).isEqualTo(1);
        assertThat(savedResume.getCareers().size()).isEqualTo(1);
        assertThat(savedResume.getCertificates().size()).isEqualTo(1);

    }

    private Users createUser(String nickname, String gender, String birthday, String ageRange, String profileImageUrl) {
        return Users.builder()
                .nickname(nickname)
                .oAuthProvider(null)
                .gender(gender)
                .birthday(birthday)
                .ageRange(ageRange)
                .profileImageUrl(profileImageUrl)
                .build();
    }

    private Resume createResume(String introduce, String photoUrl, String occupation, Boolean isOpened, String name, Users users) {
        return Resume.builder()
                .introduce(introduce)
                .photo_url(photoUrl)
                .occupation(occupation)
                .isOpened(isOpened)
                .name(name)
                .users(users)
                .build();
    }

    private Certificate createCertificate(String name, String rating, int issuedYear, int issuedMonth, Boolean isIssued) {
        return Certificate.builder()
                .name(name)
                .rating(rating)
                .issuedYear(issuedYear)
                .issuedMonth(issuedMonth)
                .isIssued(isIssued)
                .build();
    }

    private Career createCareer(int startedAt, int endedAt, String company, String title, Boolean isAttendanced, String content) {
        return Career.builder()
                .startedAt(startedAt)
                .endedAt(endedAt)
                .company(company)
                .title(title)
                .isAttendanced(isAttendanced)
                .content(content)
                .build();
    }

    private Education createEducation(String institution, String process, int startedAt, int endedAt, String content, Boolean isprocessed) {
        return Education.builder()
                .institution(institution)
                .process(process)
                .startedAt(startedAt)
                .endedAt(endedAt)
                .content(content)
                .isProcessed(isprocessed)
                .build();
    }
}
