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
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${photo.url}")
    private String photoUrl;

    private final S3Uploader s3Uploader;
    @Transactional
    public Long addResume(SaveResumeReq resumeReq, BindingResult bindingResult, Long userId) throws IOException {
        if (resumeRepository.findByUsersId(userId).isPresent()) {
            throw new IllegalStateException("이미 해당 유저의 이력서가 존재합니다.");
        }
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

        if(!resumeReq.getImage().isEmpty()) {
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
        Resume savedResume = resumeRepository.save(resume);
        return savedResume.getId();
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

    @Transactional
    public Long modifyResume(Long resumeId, ResumeDto.ModifyResumeReq resumeReq, BindingResult bindingResult, Long userId) throws IOException {
        Resume resume = resumeRepository.findById(resumeId).orElseThrow(
                () ->new NotFoundException()
        );

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

        if(!resumeReq.getImage().isEmpty()) {
            photoUrl = s3Uploader.upload(resumeReq.getImage(), "images");
        }
        resume.uploadPhotoUrl(photoUrl);
        resume.update(resumeReq, photoUrl);

        resume.getCareers().clear();
        resume.getCertificates().clear();
        resume.getEducations().clear();

        for(CareerDto.modifyCareerReq modifyCareerReq : resumeReq.getCareerList()){
            Career career = Career.from(modifyCareerReq);
            resume.addCareer(career);
        }

        for(CertificateDto.modifyCertificateReq modifyCertificateReq : resumeReq.getCertificateList()){
            Certificate certificate = Certificate.from(modifyCertificateReq);
            resume.addCertificate(certificate);
        }

        for(EducationDto.modifyEducationReq modifyEducationReq : resumeReq.getEducationList()) {
            Education education = Education.from(modifyEducationReq);
            resume.addEducation(education);
        }
        Resume savedResume = resumeRepository.save(resume);

        return savedResume.getId();
    }

    @Transactional
    public void removeResume(Long resumeId, Long userId){
        Users user =  usersRepository.findById(userId).orElseThrow(
                () -> new NotFoundException()
        );
        resumeRepository.deleteById(resumeId);
    }
}
