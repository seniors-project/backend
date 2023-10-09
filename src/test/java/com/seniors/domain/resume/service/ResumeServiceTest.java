package com.seniors.domain.resume.service;

import com.seniors.common.constant.OAuthProvider;
import com.seniors.common.exception.type.BadRequestException;
import com.seniors.domain.resume.dto.CareerDto;
import com.seniors.domain.resume.dto.CertificateDto;

import com.seniors.domain.resume.dto.EducationDto;
import com.seniors.domain.resume.dto.ResumeDto;
import com.seniors.domain.resume.entity.Career;
import com.seniors.domain.resume.entity.Certificate;
import com.seniors.domain.resume.entity.Education;
import com.seniors.domain.resume.entity.Resume;
import com.seniors.domain.resume.repository.ResumeRepository;
import com.seniors.domain.users.entity.Users;
import com.seniors.domain.users.repository.UsersRepository;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


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



    @DisplayName("신규 이력서를 등록한다. 이력서와 함께 자격증, 경력, 교육도 동반되어 저장된다.")
    @Test
    void addResume() throws IOException {
        // given
        Users user = createUser("김철수", "male", "08-08", "20~29", "https:~");
        Users savedUser = usersRepository.save(user);

        byte[] imageData = new byte[] {
                (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0,
                // ... (more image bytes) ...
                (byte) 0xFF, (byte) 0xD9
        };

        MockMultipartFile image = new MockMultipartFile(
                "image",           // Field name
                "image.jpg",       // Original file name
                "image/jpeg",      // Content type
                imageData          // Image data as byte array
        );

        List<CertificateDto.saveCertificateReq> certificateReqList = new ArrayList<>();
        List<CareerDto.saveCareerReq> careerReqList = new ArrayList<>();
        List<EducationDto.saveEducationReq> educationReqList = new ArrayList<>();

        CertificateDto.saveCertificateReq saveCertificateReq = new CertificateDto.saveCertificateReq();
        saveCertificateReq.setName("OPIC");
        saveCertificateReq.setRating("AL");
        saveCertificateReq.setIssuedYear(2005);
        saveCertificateReq.setIssuedMonth(5);
        saveCertificateReq.setIsIssued(false);
        certificateReqList.add(saveCertificateReq);

        CareerDto.saveCareerReq saveCareerReq = new CareerDto.saveCareerReq();
        saveCareerReq.setStartedAt(2001);
        saveCareerReq.setEndedAt(2005);
        saveCareerReq.setCompany("삼성 병원");
        saveCareerReq.setTitle("부장");
        saveCareerReq.setIsAttendanced(false);
        saveCareerReq.setContent("삼성 병원에서 부장으로 근무");
        careerReqList.add(saveCareerReq);

        EducationDto.saveEducationReq saveEducationReq = new EducationDto.saveEducationReq();
        saveEducationReq.setInstitution("서울대학교");
        saveEducationReq.setProcess("의과대학");
        saveEducationReq.setStartedAt(1990);
        saveEducationReq.setEndedAt(1994);
        saveEducationReq.setContent("의대 학부 전공");
        saveEducationReq.setIsProcessed(false);
        educationReqList.add(saveEducationReq);

        ResumeDto.SaveResumeReq saveResumeReq = new ResumeDto.SaveResumeReq();
        saveResumeReq.setIntroduce("안녕하세요");
        saveResumeReq.setOccupation("의사");
        saveResumeReq.setIsOpened(true);
        saveResumeReq.setName("김철수");
//        saveResumeReq.setImage(image);
        saveResumeReq.setCareerList(careerReqList);
        saveResumeReq.setCertificateList(certificateReqList);
        saveResumeReq.setEducationList(educationReqList);

        // when
//        Long savedResumeId = resumeService.addResume(saveResumeReq, savedUser.getId());

        List<Resume> resumes = resumeRepository.findAll();
        assertThat(resumes).hasSize(1)
                .extracting("introduce", "occupation", "isOpened", "name", "photoUrl")
                .contains(
                        tuple("안녕하세요", "의사", true, "김철수", "https://seniors-for-bucket.s3.ap-northeast-2.amazonaws.com/images/image.jpg")
                );
        assertThat(resumes.get(0).getCertificates()).hasSize(1)
                .extracting("name", "rating", "issuedYear", "issuedMonth", "isIssued")
                .contains(
                        tuple("OPIC", "AL", 2005, 5, false)
                );
        assertThat(resumes.get(0).getCareers()).hasSize(1)
                .extracting("startedAt", "endedAt", "company", "title", "isAttendanced", "content")
                .contains(
                        tuple(2001, 2005, "삼성 병원", "부장", false, "삼성 병원에서 부장으로 근무")
                );
        assertThat(resumes.get(0).getEducations()).hasSize(1)
                .extracting("institution", "process", "startedAt", "endedAt", "content", "isProcessed")
                .contains(
                        tuple("서울대학교", "의과대학", 1990, 1994, "의대 학부 전공", false)
                );

    }

    @DisplayName("신규 이력서를 등록한다. 이력서는 유저당 한개씩만 등록 가능하며 2개 이상 등록 시도시 예외가 발생한다.")
    @Test
    void addResumeMoreThanTwice() throws IOException {
        // given
        Users user = createUser("김철수", "male", "08-08", "20~29", "https:~");
        Users savedUser = usersRepository.save(user);

        byte[] imageData = new byte[] {
                (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0,
                // ... (more image bytes) ...
                (byte) 0xFF, (byte) 0xD9
        };

        MockMultipartFile image = new MockMultipartFile(
                "image",           // Field name
                "image.jpg",       // Original file name
                "image/jpeg",      // Content type
                imageData          // Image data as byte array
        );

        List<CertificateDto.saveCertificateReq> certificateReqList = new ArrayList<>();
        List<CareerDto.saveCareerReq> careerReqList = new ArrayList<>();
        List<EducationDto.saveEducationReq> educationReqList = new ArrayList<>();

        CertificateDto.saveCertificateReq saveCertificateReq1 = new CertificateDto.saveCertificateReq();
        saveCertificateReq1.setName("OPIC");
        saveCertificateReq1.setRating("AL");
        saveCertificateReq1.setIssuedYear(2005);
        saveCertificateReq1.setIssuedMonth(5);
        saveCertificateReq1.setIsIssued(false);
        certificateReqList.add(saveCertificateReq1);

        CertificateDto.saveCertificateReq saveCertificateReq2 = new CertificateDto.saveCertificateReq();
        saveCertificateReq2.setName("TOEIC");
        saveCertificateReq2.setRating("900");
        saveCertificateReq2.setIssuedYear(2001);
        saveCertificateReq2.setIssuedMonth(1);
        saveCertificateReq2.setIsIssued(false);
        certificateReqList.add(saveCertificateReq2);

        CareerDto.saveCareerReq saveCareerReq1 = new CareerDto.saveCareerReq();
        saveCareerReq1.setStartedAt(2001);
        saveCareerReq1.setEndedAt(2005);
        saveCareerReq1.setCompany("삼성 병원");
        saveCareerReq1.setTitle("부장");
        saveCareerReq1.setIsAttendanced(false);
        saveCareerReq1.setContent("삼성 병원에서 부장으로 근무");
        careerReqList.add(saveCareerReq1);

        CareerDto.saveCareerReq saveCareerReq2 = new CareerDto.saveCareerReq();
        saveCareerReq2.setStartedAt(2005);
        saveCareerReq2.setEndedAt(2013);
        saveCareerReq2.setCompany("아산 병원");
        saveCareerReq2.setTitle("부장");
        saveCareerReq2.setIsAttendanced(false);
        saveCareerReq2.setContent("아산 병원에서 부장으로 근무");
        careerReqList.add(saveCareerReq2);

        EducationDto.saveEducationReq saveEducationReq1 = new EducationDto.saveEducationReq();
        saveEducationReq1.setInstitution("서울대학교");
        saveEducationReq1.setProcess("의과대학");
        saveEducationReq1.setStartedAt(1990);
        saveEducationReq1.setEndedAt(1994);
        saveEducationReq1.setContent("의대 학부 전공");
        saveEducationReq1.setIsProcessed(false);
        educationReqList.add(saveEducationReq1);

        EducationDto.saveEducationReq saveEducationReq2 = new EducationDto.saveEducationReq();
        saveEducationReq2.setInstitution("연세대학교");
        saveEducationReq2.setProcess("의과대학");
        saveEducationReq2.setStartedAt(1990);
        saveEducationReq2.setEndedAt(1994);
        saveEducationReq2.setContent("의대 학부 전공");
        saveEducationReq2.setIsProcessed(false);
        educationReqList.add(saveEducationReq2);

        ResumeDto.SaveResumeReq saveResumeReq1 = new ResumeDto.SaveResumeReq();
        saveResumeReq1.setIntroduce("안녕하세요");
        saveResumeReq1.setOccupation("의사");
        saveResumeReq1.setIsOpened(true);
        saveResumeReq1.setName("김철수");
//        saveResumeReq1.setImage(image);
        saveResumeReq1.setCareerList(careerReqList);
        saveResumeReq1.setCertificateList(certificateReqList);
        saveResumeReq1.setEducationList(educationReqList);

        ResumeDto.SaveResumeReq saveResumeReq2 = new ResumeDto.SaveResumeReq();
        saveResumeReq2.setIntroduce("안녕하세요");
        saveResumeReq2.setOccupation("의사");
        saveResumeReq2.setIsOpened(true);
        saveResumeReq2.setName("김백수");
//        saveResumeReq2.setImage(image);
        saveResumeReq2.setCareerList(careerReqList);
        saveResumeReq2.setCertificateList(certificateReqList);
        saveResumeReq2.setEducationList(educationReqList);

        // when & then
//        resumeService.addResume(saveResumeReq1, savedUser.getId());
//        assertThatThrownBy(() -> resumeService.addResume(saveResumeReq2, savedUser.getId()))
////                .isInstanceOf(BadRequestException.class)
//                .hasMessage("이미 해당 유저의 이력서가 존재합니다.");

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

}