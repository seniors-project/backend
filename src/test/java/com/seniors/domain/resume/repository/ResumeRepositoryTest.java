package com.seniors.domain.resume.repository;

import com.seniors.domain.resume.dto.ResumeDto;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Book;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
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
    @DisplayName("이력서를 단건 저장한다. 연관관계에 있는 자격증, 경력, 교육사항들도 함께 저장된다.")
    void addResume() {
        // given
        Resume resume = createResume("안녕하세요~!!",  "개발자", "박철수", null);
        Certificate certificate = createCertificate("OPIC", "AL", 2015, 8, true);
        Career career = createCareer(1998, 2007, "Naver", "CTO", false, "정산 업무 담당");
        Education education = createEducation("ABC 대학교", "컴퓨터 공학과", 1980, 1984, "컴퓨터 공학과 전공했습니다.", false);

        resume.addCertificate(certificate);
        resume.addCareer(career);
        resume.addEducation(education);

        // when
        Resume savedResume = resumeRepository.save(resume);

        // then
        assertThat(resume).isEqualTo(savedResume);
        assertThat(resume.getIntroduce()).isEqualTo(savedResume.getIntroduce());
        assertThat(resume.getOccupation()).isEqualTo(savedResume.getOccupation());
        assertThat(resume.getName()).isEqualTo(savedResume.getName());
        assertThat(savedResume.getId()).isNotNull();
        assertThat(certificate.getName()).isEqualTo(savedResume.getCertificates().get(0).getName());
        assertThat(certificate.getRating()).isEqualTo(savedResume.getCertificates().get(0).getRating());
        assertThat(career.getCompany()).isEqualTo(savedResume.getCareers().get(0).getCompany());
        assertThat(career.getTitle()).isEqualTo(savedResume.getCareers().get(0).getTitle());
        assertThat(education.getInstitution()).isEqualTo(savedResume.getEducations().get(0).getInstitution());
        assertThat(education.getProcess()).isEqualTo(savedResume.getEducations().get(0).getProcess());
    }

    @Test
    @DisplayName("이력서를 pk를 통해 단건 조회한다. 연관관계에 있는 자격증, 경력, 교육사항들도 함께 조회된다.")
    void findResume() {
        // given
        Resume resume = createResume("안녕하세요~!!",  "개발자", "박철수", null);
        Certificate certificate = createCertificate("OPIC", "AL", 2015, 8, true);
        Career career = createCareer(1998, 2007, "Naver", "CTO", false, "정산 업무 담당");
        Education education = createEducation("ABC 대학교", "컴퓨터 공학과", 1980, 1984, "컴퓨터 공학과 전공했습니다.", false);

        resume.addCertificate(certificate);
        resume.addCareer(career);
        resume.addEducation(education);
        Resume savedResume = resumeRepository.save(resume);

        // when
        Resume findResume = resumeRepository.findById(savedResume.getId()).get();

        // then
        assertThat(resume.getIntroduce()).isEqualTo(findResume.getIntroduce());
        assertThat(resume.getOccupation()).isEqualTo(findResume.getOccupation());
        assertThat(resume.getName()).isEqualTo(findResume.getName());
        assertThat(certificate.getName()).isEqualTo(findResume.getCertificates().get(0).getName());
        assertThat(certificate.getRating()).isEqualTo(findResume.getCertificates().get(0).getRating());
        assertThat(career.getCompany()).isEqualTo(findResume.getCareers().get(0).getCompany());
        assertThat(career.getTitle()).isEqualTo(findResume.getCareers().get(0).getTitle());
        assertThat(education.getInstitution()).isEqualTo(findResume.getEducations().get(0).getInstitution());
        assertThat(education.getProcess()).isEqualTo(findResume.getEducations().get(0).getProcess());
    }

    @Test
    @DisplayName("이력서를 유저의 id를 통해 단건 조회한다. 연관관계에 있는 자격증, 경력, 교육사항들도 함께 조회된다.")
    void findResumeByUsers() {
        // given
        Users users = createUser("mike", "male", "08-08", "20~29");
        Users savedUser = usersRepository.save(users);

        Resume resume = createResume("안녕하세요~!!",  "개발자", "박철수", savedUser);
        Certificate certificate = createCertificate("OPIC", "AL", 2015, 8, true);
        Career career = createCareer(1998, 2007, "Naver", "CTO", false, "정산 업무 담당");
        Education education = createEducation("ABC 대학교", "컴퓨터 공학과", 1980, 1984, "컴퓨터 공학과 전공했습니다.", false);

        resume.addCertificate(certificate);
        resume.addCareer(career);
        resume.addEducation(education);
        Resume savedResume = resumeRepository.save(resume);

        // when
        Resume findResume = resumeRepository.findByUsersId(savedResume.getUsers().getId()).get();

        // then
        assertThat(resume.getIntroduce()).isEqualTo(findResume.getIntroduce());
        assertThat(resume.getOccupation()).isEqualTo(findResume.getOccupation());
        assertThat(resume.getName()).isEqualTo(findResume.getName());
        assertThat(certificate.getName()).isEqualTo(findResume.getCertificates().get(0).getName());
        assertThat(certificate.getRating()).isEqualTo(findResume.getCertificates().get(0).getRating());
        assertThat(career.getCompany()).isEqualTo(findResume.getCareers().get(0).getCompany());
        assertThat(career.getTitle()).isEqualTo(findResume.getCareers().get(0).getTitle());
        assertThat(education.getInstitution()).isEqualTo(findResume.getEducations().get(0).getInstitution());
        assertThat(education.getProcess()).isEqualTo(findResume.getEducations().get(0).getProcess());
    }


    @Test
    @DisplayName("이력서의 열람 횟수가 1 증가한다.")
    void increaseResumeViewCount(){
        // given
        Resume resume = createResume("안녕하세요~!!",  "개발자", "박철수", null);
        Resume savedResume = resumeRepository.save(resume);

        // when
        resumeRepository.increaseViewCount(resume.getId());
        Resume updatedResume = resumeRepository.findById(savedResume.getId()).get();

        // then
        assertThat(savedResume.getViewCount()).isEqualTo(0);
        assertThat(updatedResume.getViewCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("이력서 리스트를 페이지 사이즈 만큼 조회한다.")
    void findResumeList(){
        // given
        Users user1 = createUser("mike", "male", "08-08", "20~29");
        Users user2 = createUser("nike", "male", "02-02", "20~29");
        Users user3 = createUser("ann", "female", "03-01", "20~29");
        Users user4 = createUser("bill", "female", "04-08", "20~29");
        Users savedUser1 = usersRepository.save(user1);
        Users savedUser2 = usersRepository.save(user2);
        Users savedUser3 = usersRepository.save(user3);
        Users savedUser4 = usersRepository.save(user4);

        Resume resume1 = createResume("안녕하세요~!!",  "개발자", "박철수", savedUser1);
        Resume resume2 = createResume("Hello",  "의사", "김철수", savedUser2);
        Resume resume3 = createResume("안녕!!",  "요리사", "이철수", savedUser3);
        Resume resume4 = createResume("안녕요~!!",  "회사원", "송철수", savedUser4);

        Resume savedResume1 = resumeRepository.save(resume1);
        Resume savedResume2 = resumeRepository.save(resume2);
        Resume savedResume3 = resumeRepository.save(resume3);
        Resume savedResume4 = resumeRepository.save(resume4);
        Pageable pageable = PageRequest.of(0, 2);

        // when
        Slice<ResumeDto.GetResumeByQueryDslRes> resumeList = resumeRepository.findResumeList(pageable, null, null);

        // then
        assertThat(resumeList.getContent().size()).isEqualTo(pageable.getPageSize());
        assertThat(resumeList.getContent().get(0).getId()).isEqualTo(savedResume4.getId());
        assertThat(resumeList.getContent().get(0).getIntroduce()).isEqualTo(resume4.getIntroduce());
        assertThat(resumeList.getContent().get(0).getOccupation()).isEqualTo(resume4.getOccupation());
        assertThat(resumeList.getContent().get(0).getName()).isEqualTo(resume4.getName());
        assertThat(resumeList.getContent().get(1).getId()).isEqualTo(savedResume3.getId());
        assertThat(resumeList.getContent().get(1).getIntroduce()).isEqualTo(resume3.getIntroduce());
        assertThat(resumeList.getContent().get(1).getOccupation()).isEqualTo(resume3.getOccupation());
        assertThat(resumeList.getContent().get(1).getName()).isEqualTo(resume3.getName());
    }

    @Test
    @DisplayName("이력서를 단건 삭제한다.")
    void deleteResume(){
        // given
        Resume resume = createResume("안녕하세요~!!",  "개발자", "박철수", null);
        Resume savedResume = resumeRepository.save(resume);

        // when
        resumeRepository.delete(savedResume);
        Optional<Resume> findResume = resumeRepository.findById(savedResume.getId());

        // then
        assertThatThrownBy(() -> findResume.get())
                .isInstanceOf(NoSuchElementException.class);
    }

    private Users createUser(String nickname, String gender, String birthday, String ageRange) {
        return Users.builder()
                .nickname(nickname)
                .gender(gender)
                .birthday(birthday)
                .ageRange(ageRange)
                .build();
    }

    private Resume createResume(String introduce, String occupation, String name, Users users) {
        return Resume.builder()
                .introduce(introduce)
                .occupation(occupation)
                .isOpened(true)
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
