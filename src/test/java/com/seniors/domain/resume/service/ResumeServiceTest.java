package com.seniors.domain.resume.service;

import com.seniors.common.constant.OAuthProvider;
import com.seniors.common.dto.CustomSlice;
import com.seniors.common.exception.type.BadRequestException;
import com.seniors.common.exception.type.ConflictException;
import com.seniors.common.exception.type.ForbiddenException;
import com.seniors.domain.resume.dto.*;

import com.seniors.domain.resume.entity.*;
import com.seniors.domain.resume.repository.ResumeRepository;
import com.seniors.domain.resume.repository.ResumeViewRepository;
import com.seniors.domain.users.entity.Users;
import com.seniors.domain.users.repository.UsersRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;


@Transactional
@SpringBootTest
@ActiveProfiles("dev")
class ResumeServiceTest {

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private ResumeViewRepository resumeViewRepository;



    @DisplayName("신규 이력서를 등록한다. 자격증, 경력, 교육사항들도 함께 등록된다.")
    @Test
    void addResume() throws IOException {
        // given
        Users user = createUser("mike", "male", "08-08", "20~29");
        Users savedUser = usersRepository.save(user);

        List<CertificateDto.saveCertificateReq> certificateReqList = new ArrayList<>();
        List<CareerDto.saveCareerReq> careerReqList = new ArrayList<>();
        List<EducationDto.saveEducationReq> educationReqList = new ArrayList<>();

        CertificateDto.saveCertificateReq certificateReq = new CertificateDto.saveCertificateReq();
        certificateReq.setName("OPIC");
        certificateReq.setIssuedYear(2005);
        certificateReq.setIssuedMonth(5);
        certificateReq.setRating("AL");
        certificateReqList.add(certificateReq);

        CareerDto.saveCareerReq careerReq = new CareerDto.saveCareerReq();
        careerReq.setStartedAt(2001);
        careerReq.setCompany("삼성 병원");
        careerReq.setTitle("부장");
        careerReq.setContent("삼성 병원에서 부장으로 근무");
        careerReqList.add(careerReq);

        EducationDto.saveEducationReq educationReq = new EducationDto.saveEducationReq();
        educationReq.setInstitution("서울대학교");
        educationReq.setStartedAt(1990);
        educationReq.setProcess("컴공");
        educationReq.setContent("학부 전공");
        educationReqList.add(educationReq);

        ResumeDto.SaveResumeReq resumeReq = new ResumeDto.SaveResumeReq();
        resumeReq.setOccupation("의사");
        resumeReq.setName("김철수");
        resumeReq.setCareers(careerReqList);
        resumeReq.setCertificates(certificateReqList);
        resumeReq.setEducations(educationReqList);

        // when
        Long newResumeId = resumeService.addResume(resumeReq, null, savedUser.getId());

        // then
        Resume findResume = resumeRepository.findById(newResumeId).get();
        assertThat(resumeReq.getOccupation()).isEqualTo(findResume.getOccupation());
        assertThat(resumeReq.getName()).isEqualTo(findResume.getName());
        assertThat(certificateReq.getName()).isEqualTo(findResume.getCertificates().get(0).getName());
        assertThat(certificateReq.getIssuedYear()).isEqualTo(findResume.getCertificates().get(0).getIssuedYear());
        assertThat(certificateReq.getIssuedMonth()).isEqualTo(findResume.getCertificates().get(0).getIssuedMonth());
        assertThat(careerReq.getStartedAt()).isEqualTo(findResume.getCareers().get(0).getStartedAt());
        assertThat(careerReq.getCompany()).isEqualTo(findResume.getCareers().get(0).getCompany());
        assertThat(careerReq.getTitle()).isEqualTo(findResume.getCareers().get(0).getTitle());
        assertThat(careerReq.getContent()).isEqualTo(findResume.getCareers().get(0).getContent());
        assertThat(educationReq.getInstitution()).isEqualTo(findResume.getEducations().get(0).getInstitution());
        assertThat(educationReq.getStartedAt()).isEqualTo(findResume.getEducations().get(0).getStartedAt());
    }

    @DisplayName("신규 이력서를 등록한다. 이력서는 유저당 한개씩만 등록 가능하며 2개 이상 등록 시도시 예외가 발생한다.")
    @Test
    void addResumeMoreThanTwice() {
        // given
        Users user = createUser("mike", "male", "08-08", "20~29");
        Users savedUser = usersRepository.save(user);

        Resume resume1 = createResume("안녕하세요~!!",  "개발자", "박철수", savedUser);
        resumeRepository.save(resume1);

        ResumeDto.SaveResumeReq resumeReq = new ResumeDto.SaveResumeReq();
        resumeReq.setOccupation("의사");
        resumeReq.setName("김철수");

        // when & then
        assertThatThrownBy(() -> resumeService.addResume(resumeReq, null, savedUser.getId()))
                .isInstanceOf(ConflictException.class)
                        .hasMessage("이미 해당 유저의 이력서가 존재합니다.");
    }

    @DisplayName("신규 이력서를 등록한다. 퇴사연도와 재직중 여부 값을 둘다 받으면 예외가 터진다.")
    @Test
    void addResumeWithConstraint1(){
        // given
        Users user = createUser("mike", "male", "08-08", "20~29");
        Users savedUser = usersRepository.save(user);

        List<CareerDto.saveCareerReq> careerReqList = new ArrayList<>();

        CareerDto.saveCareerReq careerReq = new CareerDto.saveCareerReq();
        careerReq.setStartedAt(2001);
        careerReq.setEndedAt(2008); // 퇴사 연도
        careerReq.setIsAttendanced(true); // 재직중 여부
        careerReq.setCompany("삼성 병원");
        careerReq.setTitle("부장");
        careerReq.setContent("삼성 병원에서 부장으로 근무");
        careerReqList.add(careerReq);

        ResumeDto.SaveResumeReq resumeReq = new ResumeDto.SaveResumeReq();
        resumeReq.setOccupation("의사");
        resumeReq.setName("김철수");
        resumeReq.setCareers(careerReqList);


        // when & then
        assertThatThrownBy(() -> resumeService.addResume(resumeReq, null, savedUser.getId()))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("퇴사연도를 입력하심면 재직중 여부를 체크하실 수 없습니다.");
    }

    @DisplayName("신규 이력서를 등록한다. 종료연도와 진행중 여부 값을 둘다 받으면 예외가 터진다.")
    @Test
    void addResumeWithConstraint2(){
        // given
        Users user = createUser("mike", "male", "08-08", "20~29");
        Users savedUser = usersRepository.save(user);

        List<EducationDto.saveEducationReq> educationReqList = new ArrayList<>();

        EducationDto.saveEducationReq educationReq = new EducationDto.saveEducationReq();
        educationReq.setInstitution("서울대학교");
        educationReq.setEndedAt(1998); // 종료 연도
        educationReq.setIsProcessed(true); // 진행중 여부
        educationReq.setStartedAt(1990);
        educationReqList.add(educationReq);

        ResumeDto.SaveResumeReq resumeReq = new ResumeDto.SaveResumeReq();
        resumeReq.setOccupation("의사");
        resumeReq.setName("김철수");
        resumeReq.setEducations(educationReqList);

        // when & then
        assertThatThrownBy(() -> resumeService.addResume(resumeReq, null, savedUser.getId()))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("종료연도를 입력하시면 진행중 여부를 체크하실 수 없습니다.");
    }

    @DisplayName("이력서를 조회한다. 이력서를 열람한 횟수가 1증가한다. 자격증, 경력, 교육사항들도 함께 조회된다.")
    @Test
    void findResume(){
        // given
        Users user1 = createUser("mike", "male", "08-08", "20~29");
        Users user2 = createUser("sam", "female", "03-01", "20~29");
        Users savedUser1 = usersRepository.save(user1);
        Users savedUser2 = usersRepository.save(user1);

        Resume resume = createResume("안녕하세요~!!",  "개발자", "박철수", savedUser1);
        Certificate certificate = createCertificate("OPIC", "AL", 2015, 8, true);
        Career career = createCareer(1998, 2007, "Naver", "CTO", false, "정산 업무 담당");
        Education education = createEducation("ABC 대학교", "컴퓨터 공학과", 1980, 1984, "컴퓨터 공학과 전공했습니다.", false);

        resume.addCertificate(certificate);
        resume.addCareer(career);
        resume.addEducation(education);
        Resume savedResume = resumeRepository.save(resume);

        // when
        ResumeDto.GetResumeRes getResumeRes = resumeService.findResume(savedResume.getId(), savedUser2.getId());

        // then
        assertThat(getResumeRes.getViewCount()).isEqualTo(savedResume.getViewCount()+1); // viewCount 1 증가
        assertThat(getResumeRes.getIntroduce()).isEqualTo(resume.getIntroduce());
        assertThat(getResumeRes.getOccupation()).isEqualTo(resume.getOccupation());
        assertThat(getResumeRes.getName()).isEqualTo(resume.getName());
        assertThat(getResumeRes.getCertificates().get(0).getName()).isEqualTo(certificate.getName());
        assertThat(getResumeRes.getCertificates().get(0).getRating()).isEqualTo(certificate.getRating());
        assertThat(getResumeRes.getCareers().get(0).getCompany()).isEqualTo(career.getCompany());
        assertThat(getResumeRes.getCareers().get(0).getContent()).isEqualTo(career.getContent());
        assertThat(getResumeRes.getEducations().get(0).getInstitution()).isEqualTo(education.getInstitution());
        assertThat(getResumeRes.getEducations().get(0).getProcess()).isEqualTo(education.getProcess());

    }

    @DisplayName("나의 이력서를 조회한다. 이력서를 열람한 횟수는 변화없다. 자격증, 경력, 교육사항들도 함께 조회된다.")
    @Test
    void findMyesume(){
        // given
        Users user = createUser("mike", "male", "08-08", "20~29");
        Users savedUser1 = usersRepository.save(user);

        Resume resume = createResume("안녕하세요~!!",  "개발자", "박철수", savedUser1);
        Certificate certificate = createCertificate("OPIC", "AL", 2015, 8, true);
        Career career = createCareer(1998, 2007, "Naver", "CTO", false, "정산 업무 담당");
        Education education = createEducation("ABC 대학교", "컴퓨터 공학과", 1980, 1984, "컴퓨터 공학과 전공했습니다.", false);

        resume.addCertificate(certificate);
        resume.addCareer(career);
        resume.addEducation(education);
        Resume savedResume = resumeRepository.save(resume);

        // when
        ResumeDto.GetResumeRes getResumeRes = resumeService.findMyResume(user.getId());

        // then
        assertThat(getResumeRes.getViewCount()).isEqualTo(savedResume.getViewCount());
        assertThat(getResumeRes.getIntroduce()).isEqualTo(resume.getIntroduce());
        assertThat(getResumeRes.getOccupation()).isEqualTo(resume.getOccupation());
        assertThat(getResumeRes.getName()).isEqualTo(resume.getName());
        assertThat(getResumeRes.getCertificates().get(0).getName()).isEqualTo(certificate.getName());
        assertThat(getResumeRes.getCertificates().get(0).getRating()).isEqualTo(certificate.getRating());
        assertThat(getResumeRes.getCareers().get(0).getCompany()).isEqualTo(career.getCompany());
        assertThat(getResumeRes.getCareers().get(0).getContent()).isEqualTo(career.getContent());
        assertThat(getResumeRes.getEducations().get(0).getInstitution()).isEqualTo(education.getInstitution());
        assertThat(getResumeRes.getEducations().get(0).getProcess()).isEqualTo(education.getProcess());

    }

    @DisplayName("이력서 리스트를 페이지 사이즈 만큼 조회한다.")
    @Test
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
        CustomSlice<ResumeDto.GetResumeByQueryDslRes> getResumeByQueryDslResCustomSlice = resumeService.findResumeList(pageable, null, user1.getId());

        // then
        assertThat(getResumeByQueryDslResCustomSlice.getContent()).hasSize(pageable.getPageSize())
                .extracting("id", "introduce", "occupation", "name")
                .containsExactlyInAnyOrder(
                        tuple(savedResume4.getId(), "안녕요~!!",  "회사원", "송철수"),
                        tuple(savedResume3.getId(), "안녕!!",  "요리사", "이철수")
                );
    }

    @DisplayName("이력서를 수정한다. 자격증, 경력, 교육사항들도 함께 수정할 수 있다.")
    @Test
    void modifyResume() throws IOException {
        // given
        Users user = createUser("mike", "male", "08-08", "20~29");
        Users savedUser = usersRepository.save(user);

        Resume resume = createResume("안녕하세요~!!",  "개발자", "박철수", savedUser);
        Certificate certificate = createCertificate("OPIC", "AL", 2015, 8, true);
        Career career = createCareer(1998, 2007, "Naver", "CTO", false, "정산 업무 담당");
        Education education = createEducation("ABC 대학교", "컴퓨터 공학과", 1980, 1984, "컴퓨터 공학과 전공했습니다.", false);

        resume.addCertificate(certificate);
        resume.addCareer(career);
        resume.addEducation(education);
        Resume savedResume = resumeRepository.save(resume);

        List<CertificateDto.modifyCertificateReq> certificateReqList = new ArrayList<>();
        CertificateDto.modifyCertificateReq certificateReq = new CertificateDto.modifyCertificateReq();
        certificateReq.setName("TOEIC");
        certificateReq.setIssuedYear(1995);
        certificateReq.setIssuedMonth(5);
        certificateReqList.add(certificateReq);

        ResumeDto.ModifyResumeReq resumeReq = new ResumeDto.ModifyResumeReq();
        resumeReq.setOccupation("의사");
        resumeReq.setName("김철수");
        resumeReq.setCertificates(certificateReqList);


        // when
        resumeService.modifyResume(savedResume.getId(), resumeReq, null, savedUser.getId());

        // then
        Resume modifiedResume = resumeRepository.findById(savedResume.getId()).get();
        assertThat(modifiedResume.getId()).isEqualTo(savedResume.getId());
        assertThat(modifiedResume.getOccupation()).isEqualTo("의사");
        assertThat(modifiedResume.getName()).isEqualTo("김철수");
        assertThat(modifiedResume.getCertificates().get(0).getName()).isEqualTo("TOEIC");
        assertThat(modifiedResume.getCertificates().get(0).getIssuedYear()).isEqualTo(1995);
        assertThat(modifiedResume.getCertificates().get(0).getIssuedMonth()).isEqualTo(5);
    }

    @DisplayName("다른 사람의 이력서를 수정하려고하면 예외가 터진다.")
    @Test
    void modifyResumeWithConstraint() throws IOException{
        // given
        Users user1 = createUser("mike", "male", "08-08", "20~29");
        Users user2 = createUser("sam", "female", "01-08", "20~29");
        Users savedUser1 = usersRepository.save(user1);
        Users savedUser2 = usersRepository.save(user2);

        Resume resume = createResume("안녕하세요~!!",  "개발자", "박철수", user1);
        Resume savedResume = resumeRepository.save(resume);

        ResumeDto.ModifyResumeReq resumeReq = new ResumeDto.ModifyResumeReq();
        resumeReq.setOccupation("의사");
        resumeReq.setName("김철수");

        // when & then
        assertThatThrownBy(() -> resumeService.modifyResume(savedResume.getId(), resumeReq, null, savedUser2.getId()))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("수정 권한이 없습니다.");
    }

    @DisplayName("이력서를 삭제한다. 연관된 자격증, 경력, 교육사항들도 함께 삭제된다.")
    @Test
    void removeResume() throws IOException{
        // given
        Users user = createUser("mike", "male", "08-08", "20~29");
        Users savedUser = usersRepository.save(user);

        Resume resume = createResume("안녕하세요~!!",  "개발자", "박철수", savedUser);
        Certificate certificate = createCertificate("OPIC", "AL", 2015, 8, true);
        Career career = createCareer(1998, 2007, "Naver", "CTO", false, "정산 업무 담당");
        Education education = createEducation("ABC 대학교", "컴퓨터 공학과", 1980, 1984, "컴퓨터 공학과 전공했습니다.", false);

        resume.addCertificate(certificate);
        resume.addCareer(career);
        resume.addEducation(education);
        Resume savedResume = resumeRepository.save(resume);

        // when
        resumeService.removeResume(savedResume.getId(), savedResume.getUsers().getId());

        // then
        Optional<Resume> findResume = resumeRepository.findById(savedResume.getId());
        assertThatThrownBy(() -> findResume.get())
                .isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("다른 사람의 이력서를 삭제하려고하면 예외가 터진다.")
    @Test
    void removeResumeWithConstraint(){
        // given
        Users user1 = createUser("mike", "male", "08-08", "20~29");
        Users user2 = createUser("sam", "female", "04-08", "20~29");
        Users savedUser1 = usersRepository.save(user1);
        Users savedUser2 = usersRepository.save(user2);

        Resume resume = createResume("안녕하세요~!!",  "개발자", "박철수", savedUser1);
        Certificate certificate = createCertificate("OPIC", "AL", 2015, 8, true);
        Career career = createCareer(1998, 2007, "Naver", "CTO", false, "정산 업무 담당");
        Education education = createEducation("ABC 대학교", "컴퓨터 공학과", 1980, 1984, "컴퓨터 공학과 전공했습니다.", false);

        resume.addCertificate(certificate);
        resume.addCareer(career);
        resume.addEducation(education);
        Resume savedResume = resumeRepository.save(resume);

        // when & then
        assertThatThrownBy(() -> resumeService.removeResume(savedResume.getId(), savedUser2.getId()))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("삭제 권한이 없습니다.");
    }

    @DisplayName("이력서를 열람한 사람들 정보를 조회한다.")
    @Test
    void findResumeViewerList() {
        // given
        Users user1 = createUser("mike", "male", "08-08", "20~29");
        Users user2 = createUser("sam", "female", "04-08", "20~29");
        Users user3 = createUser("tony", "female", "03-28", "20~29");

        Users savedUser1 = usersRepository.save(user1);
        Users savedUser2 = usersRepository.save(user2);
        Users savedUser3 = usersRepository.save(user3);

        Resume resume1 = createResume("안녕하세요~!!",  "의사", "박철수", savedUser1);
        resumeRepository.save(resume1);

        ResumeView resumeView2 = ResumeView.of(resume1, user2);
        ResumeView resumeView3 = ResumeView.of(resume1, user3);
        resumeViewRepository.save(resumeView2);
        resumeViewRepository.save(resumeView3);

        // when
        List<ViewerInfoDto.GetViewerInfoRes> getViewerInfoResList = resumeService.findResumeViewerList(savedUser1.getId());

        // then
        assertThat(getViewerInfoResList).hasSize(2)
                .extracting("userId", "name")
                .containsExactlyInAnyOrder(
                        tuple(savedUser3.getId(), "tony"),
                        tuple(savedUser2.getId(), "sam")
                );
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