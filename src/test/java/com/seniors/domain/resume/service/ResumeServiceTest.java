package com.seniors.domain.resume.service;

import com.seniors.common.constant.OAuthProvider;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@Transactional
@SpringBootTest
@ActiveProfiles("dev")
class ResumeServiceTest {

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Test
    @DisplayName("이력서 등록하기 테스트")
    void addResume() throws IOException {
        // given
        Users users = Users.builder()
                .snsId("aewvewvewvwe")
                .email("abc@naver.com")
                .nickname("apple")
                .oAuthProvider(null)
                .gender("male")
                .birthday("08-18")
                .ageRange("20~29")
                .profileImageUrl("https:~")
                .build();
        Users savedUser = usersRepository.save(users);
        Resume resume = Resume.builder()
                .introduce("안녕하세요 !!!")
                .photo_url("https:~~")
                .occupation("개발자")
                .isOpened(true)
                .name("박철수")
                .users(savedUser)
                .build();

        Certificate certificate = Certificate.builder()
                .name("OPIC")
                .rating("AL")
                .issuedYear(2015)
                .issuedMonth(8)
                .isIssued(true)
                .build();
        resume.addCertificate(certificate);
        Career career = Career.builder()
                .startedAt(1998)
                .endedAt(2007)
                .company("Naver")
                .title("CTO")
                .isAttendanced(false)
                .content("정산 업무 담당")
                .build();
        resume.addCareer(career);
        Education education = Education.builder()
                .institution("ABC 대학교")
                .process("컴퓨터 공학과")
                .startedAt(1980)
                .endedAt(1984)
                .content("컴퓨터 공학 전공했습니다.")
                .isProcessed(false)
                .build();
        resume.addEducation(education);

        // when
        Resume savedResume = resumeRepository.save(resume);

        // then
        assertEquals(savedResume, resume);

    }

    @Test
    @DisplayName("이력서 상세 조회 테스트")
    void findResume() {
        // given
        Users users = Users.builder()
                .snsId("aewvewvewvwe")
                .email("abc@naver.com")
                .nickname("apple")
                .oAuthProvider(null)
                .gender("male")
                .birthday("08-18")
                .ageRange("20~29")
                .profileImageUrl("https:~")
                .build();
        Users savedUser = usersRepository.save(users);
        Resume resume = Resume.builder()
                .introduce("안녕하세요 !!!")
                .photo_url("https:~~")
                .occupation("개발자")
                .isOpened(true)
                .name("박철수")
                .users(savedUser)
                .build();

        Certificate certificate = Certificate.builder()
                .name("OPIC")
                .rating("AL")
                .issuedYear(2015)
                .issuedMonth(8)
                .isIssued(true)
                .build();
        resume.addCertificate(certificate);
        Career career = Career.builder()
                .startedAt(1998)
                .endedAt(2007)
                .company("Naver")
                .title("CTO")
                .isAttendanced(false)
                .content("정산 업무 담당")
                .build();
        resume.addCareer(career);
        Education education = Education.builder()
                .institution("ABC 대학교")
                .process("컴퓨터 공학과")
                .startedAt(1980)
                .endedAt(1984)
                .content("컴퓨터 공학 전공했습니다.")
                .isProcessed(false)
                .build();
        resume.addEducation(education);
        Resume savedResume = resumeRepository.save(resume);

        // when
        Resume findResume = resumeRepository.findById(savedResume.getId())
                .orElseThrow(() ->new IllegalArgumentException());
        Assertions.assertEquals(resumeRepository.count(), 1);
        Assertions.assertEquals(savedResume, findResume);
    }

    @Test
    @DisplayName("이력서 리스트 조회 테스트")
    void findResumeList() {

        // given
        // 이력서 1
        Users user1 = Users.builder()
                .snsId("aaaaaaaaaaaa")
                .email("abc@naver.com")
                .nickname("apple")
                .oAuthProvider(null)
                .gender("male")
                .birthday("08-18")
                .ageRange("20~29")
                .profileImageUrl("https:~")
                .build();
        Users savedUser1 = usersRepository.save(user1);

        Resume resume1 = Resume.builder()
                .introduce("안녕하세요 !!!")
                .photo_url("https:~~")
                .occupation("개발자")
                .isOpened(true)
                .name("박철수")
                .users(savedUser1)
                .build();

        Certificate certificate1 = Certificate.builder()
                .name("OPIC")
                .rating("AL")
                .issuedYear(2015)
                .issuedMonth(8)
                .isIssued(true)
                .build();
        resume1.addCertificate(certificate1);

        Career career = Career.builder()
                .startedAt(1998)
                .endedAt(2007)
                .company("Naver")
                .title("CTO")
                .isAttendanced(false)
                .content("정산 업무 담당")
                .build();
        resume1.addCareer(career);

        Education education1 = Education.builder()
                .institution("ABC 대학교")
                .process("컴퓨터 공학과")
                .startedAt(1980)
                .endedAt(1984)
                .content("컴퓨터 공학 전공했습니다.")
                .isProcessed(false)
                .build();
        resume1.addEducation(education1);

        Resume savedResume1 = resumeRepository.save(resume1);

        // 이력서 2
        Users user2 = Users.builder()
                .snsId("bbbbbbbbbbbb")
                .email("cba@naver.com")
                .nickname("banana")
                .oAuthProvider(null)
                .gender("female")
                .birthday("01-28")
                .ageRange("20~29")
                .profileImageUrl("https:~")
                .build();
        Users savedUser2 = usersRepository.save(user2);

        Resume resume2 = Resume.builder()
                .introduce("안녕하세요 !!!")
                .photo_url("https:~~")
                .occupation("개발자")
                .isOpened(true)
                .name("박철수")
                .users(savedUser2)
                .build();

        Certificate certificate2 = Certificate.builder()
                .name("OPIC")
                .rating("AL")
                .issuedYear(2015)
                .issuedMonth(8)
                .isIssued(true)
                .build();
        resume2.addCertificate(certificate2);

        Career career2 = Career.builder()
                .startedAt(1998)
                .endedAt(2007)
                .company("Naver")
                .title("CTO")
                .isAttendanced(false)
                .content("정산 업무 담당")
                .build();
        resume2.addCareer(career2);

        Education education2 = Education.builder()
                .institution("ABC 대학교")
                .process("컴퓨터 공학과")
                .startedAt(1980)
                .endedAt(1984)
                .content("컴퓨터 공학 전공했습니다.")
                .isProcessed(false)
                .build();
        resume2.addEducation(education2);

        Resume savedResume2 = resumeRepository.save(resume2);

        // when
        List<Resume> resumeList = resumeRepository.findAll();

        // then
        assertEquals(2, resumeList.size());

    }
}