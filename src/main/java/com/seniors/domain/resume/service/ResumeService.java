package com.seniors.domain.resume.service;

import com.seniors.common.dto.DataResponseDto;
import com.seniors.common.exception.type.BadRequestException;
import com.seniors.common.exception.type.NotFoundException;
import com.seniors.config.S3Uploader;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.seniors.domain.resume.dto.ResumeDto.SaveResumeReq;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final UsersRepository usersRepository;

    private final S3Uploader s3Uploader;
    @Transactional
    public List<String> addResume(SaveResumeReq resumeReq, BindingResult bindingResult, Long userId) throws IOException {
        if(bindingResult.hasErrors()) {
            List<ObjectError> errors = bindingResult.getAllErrors();
            List<String> errorMessages = new ArrayList<>();

            for (ObjectError error : errors) {
                FieldError fieldError = (FieldError) error;
                String message = fieldError.getDefaultMessage();
                errorMessages.add(message);
            }
            throw new BadRequestException(String.join(", ", errorMessages));
        }
        Users user =  usersRepository.findById(userId).orElseThrow(
                () -> new NotFoundException()
        );
        String photoUrl = "https://seniors-for-bucket.s3.ap-northeast-2.amazonaws.com/%EA%B8%B0%EB%B3%B8%EC%9D%B4%EB%AF%B8%EC%A7%80.jpg";

        if(resumeReq.getImage() != null) {
            photoUrl = s3Uploader.upload(resumeReq.getImage(), "images");
        }
        Resume resume = Resume.of(resumeReq, user);
        resume.uploadPhotoUrl(photoUrl);
        for(CareerDto.saveCareerReq saveCareerReq  : resumeReq.getCareerList()){
            Career career = Career.from(saveCareerReq);
            resume.addCareer(career);
        }
        for(CertificateDto.saveCertificateReq saveCertificateReq : resumeReq.getCertificateList()){
            Certificate certificate = Certificate.from(saveCertificateReq);
            resume.addCertificate(certificate);
        }
        for(EducationDto.saveEducationReq saveEducationReq : resumeReq.getEducationList()){
            Education education = Education.from(saveEducationReq);
            resume.addEducation(education);
        }
        resumeRepository.save(resume);
        return null;
    }

    @Transactional(readOnly = true)
    public ResumeDto.GetResumeRes findResume(Long resumeId, Long userId){
        Users user =  usersRepository.findById(userId).orElseThrow(
                () -> new NotFoundException()
        );
        Resume resume =  resumeRepository.findById(resumeId).orElseThrow(
                () -> new NotFoundException()
        );

        return ResumeDto.GetResumeRes.from(resume);
    }

    @Transactional(readOnly = true)
    public DataResponseDto<Slice<ResumeDto.GetResumeByQueryDslRes>> findResumeList(Pageable pageable, Long lastId, Long userId){
        Users user =  usersRepository.findById(userId).orElseThrow(
                () -> new NotFoundException()
        );
        Slice<ResumeDto.GetResumeByQueryDslRes> result = resumeRepository.findResumeList(pageable, lastId, user.getId());


        return DataResponseDto.of(result);
    }
}
