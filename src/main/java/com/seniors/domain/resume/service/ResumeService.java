package com.seniors.domain.resume.service;

import com.seniors.common.dto.CustomSlice;
import com.seniors.common.dto.DataResponseDto;
import com.seniors.common.exception.type.*;
import com.seniors.config.S3Uploader;
import com.seniors.domain.notification.service.NotificationService;
import com.seniors.domain.resume.dto.*;
import com.seniors.domain.resume.dto.ResumeDto.SaveResumeReq;
import com.seniors.domain.resume.entity.*;
import com.seniors.domain.resume.repository.ResumeRepository;
import com.seniors.domain.resume.repository.ResumeViewRepository;
import com.seniors.domain.users.entity.Users;
import com.seniors.domain.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final UsersRepository usersRepository;
    private final NotificationService notificationService;

    private final ResumeViewRepository resumeViewRepository;

    private final S3Uploader s3Uploader;
    @Transactional
    public Long addResume(SaveResumeReq resumeReq, MultipartFile image, Long userId) throws IOException {
        if (resumeRepository.findByUsersId(userId).isPresent()) {
            throw new ConflictException("이미 해당 유저의 이력서가 존재합니다.");
        }

        resumeReq.getCareerList().stream()
                .filter(saveCareerReq -> saveCareerReq.getEndedAt()!=null && saveCareerReq.getIsAttendanced()==true)
                .findAny()
                .ifPresent(saveCareerReq -> {
                    throw new BadRequestException("퇴사연도를 입력하심면 재직중 여부를 체크하실 수 없습니다.");
                });

        resumeReq.getEducationList().stream()
                .filter(saveEducationReq -> saveEducationReq.getEndedAt()!=null && saveEducationReq.getIsProcessed()==true)
                .findAny()
                .ifPresent(saveEducationReq1 -> {
                    throw new BadRequestException("종료연도를 입력하시면 진행중 여부를 체크하실 수 없습니다.");
                });

        Users user =  usersRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("회원이 존재하지 않습니다.")
        );

        Resume resume = Resume.of(resumeReq, user);
        if(image != null) {
            String photoUrl = s3Uploader.upload(image, "resumes");
            resume.uploadPhotoUrl(photoUrl);
        }
        else{
            resume.uploadPhotoUrl(null);
        }

        resumeReq.getCareerList().stream()
                .map(Career::from)
                .forEach(resume::addCareer);

        resumeReq.getCertificateList().stream()
                .map(Certificate::from)
                .forEach(resume::addCertificate);

        resumeReq.getEducationList().stream()
                .map(Education::from)
                .forEach(resume::addEducation);

        Resume savedResume = resumeRepository.save(resume);
        return savedResume.getId();
    }

    @Transactional
    public ResumeDto.GetResumeRes findResume(Long resumeId, Long userId) {
        Users user =  usersRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("회원이 존재하지 않습니다.")
        );
        Resume resume =  resumeRepository.findById(resumeId).orElseThrow(
                () -> new NotFoundException("이력서가 존재하지 않습니다.")
        );
        Optional<ResumeView> findResumeView = resumeViewRepository.findByUsersAndResume(user, resume);
        if (!findResumeView.isPresent()) {
            resumeRepository.increaseViewCount(resume.getId());
            ResumeView resumeView = ResumeView.of(resume,user);
            resumeViewRepository.save(resumeView);
        }
        Resume changedResume =  resumeRepository.findById(resumeId).orElseThrow(
                () -> new NotFoundException("이력서가 존재하지 않습니다.")
        );

        if (!resume.getUsers().getId().equals(user.getId())) {
            notificationService.send(resume.getUsers(), resume, "누군가가 내 이력서를 조회했습니다!");
        }
        return ResumeDto.GetResumeRes.from(changedResume);
    }

    @Transactional(readOnly = true)
    public ResumeDto.GetResumeRes findMyResume(Long userId) {
        Users user =  usersRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("회원이 존재하지 않습니다.")
        );

        Optional<Resume> resume =  resumeRepository.findByUsersId(user.getId());
        if(resume.isEmpty()){
            return null;
        }
        return ResumeDto.GetResumeRes.from(resume.get());
    }

    @Transactional(readOnly = true)
    public DataResponseDto<CustomSlice<ResumeDto.GetResumeByQueryDslRes>> findResumeList(Pageable pageable, Long lastId, Long userId){
        Users user =  usersRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("회원이 존재하지 않습니다.")
        );
        Slice<ResumeDto.GetResumeByQueryDslRes> result = resumeRepository.findResumeList(pageable, lastId, user.getId());

        return DataResponseDto.of(CustomSlice.from(result));
    }

    @Transactional
    public void modifyResume(Long resumeId, ResumeDto.ModifyResumeReq resumeReq, MultipartFile image, Long userId) throws IOException {
        Resume resume = resumeRepository.findById(resumeId).orElseThrow(
                () ->new NotFoundException("이력서가 존재하지 않습니다.")
        );

        resumeReq.getCareerList().stream()
                .filter(modifyCareerReq -> modifyCareerReq.getEndedAt()!=null && modifyCareerReq.getIsAttendanced()==true)
                .findAny()
                .ifPresent(modifyCareerReq -> {
                    throw new BadRequestException("퇴사연도를 입력하심면 재직중 여부를 체크하실 수 없습니다.");
                });

        resumeReq.getEducationList().stream()
                .filter(modifyEducationReq -> modifyEducationReq.getEndedAt()!=null && modifyEducationReq.getIsProcessed()==true)
                .findAny()
                .ifPresent(modifyCareerReq -> {
                    throw new BadRequestException("종료연도를 입력하시면 진행중 여부를 체크하실 수 없습니다.");
                });

        Users user =  usersRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("회원이 존재하지 않습니다.")
        );

        if(!resume.getUsers().getId().equals(user.getId())){
            throw new ForbiddenException("수정 권한이 없습니다.");
        }

        if(!image.isEmpty()) {
            String photoUrl = s3Uploader.upload(image, "resumes");
            resume.update(resumeReq, photoUrl);
        }
        else{
            String photoUrl = null;
            resume.update(resumeReq, photoUrl);
        }

        resume.getCareers().clear();
        resume.getCertificates().clear();
        resume.getEducations().clear();

        resumeReq.getCareerList().stream()
                .map(Career::from)
                .forEach(resume::addCareer);

        resumeReq.getCertificateList().stream()
                .map(Certificate::from)
                .forEach(resume::addCertificate);

        resumeReq.getEducationList().stream()
                .map(Education::from)
                .forEach(resume::addEducation);
    }

    @Transactional
    public void removeResume(Long resumeId, Long userId){
        Resume resume = resumeRepository.findById(resumeId).orElseThrow(
                () -> new NotFoundException("이력서가 존재하지 않습니다.")
        );
        Users user =  usersRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("회원이 존재하지 않습니다.")
        );
        if (!resume.getUsers().getId().equals(user.getId())){
            throw new ForbiddenException("삭제 권한이 없습니다.");
        }
        resumeRepository.delete(resume);
    }


    @Transactional(readOnly = true)
    public List<ViewerInfoDto.GetViewerInfoRes> findResumeViewerList(Long userId) {

        Resume resume = resumeRepository.findByUsersId(userId).orElseThrow(
                () -> new NotFoundException("이력서가 존재하지 않습니다.")
        );

        Optional<List<ResumeView>> resumeViewList = resumeViewRepository.findByResumeId(resume.getId());
        if (resumeViewList.isEmpty()){
            return null;
        }
        List<ViewerInfoDto.GetViewerInfoRes> viewerInfoList = new ArrayList<>();

        for(ResumeView resumeView : resumeViewList.get()){
            viewerInfoList.add(ViewerInfoDto.GetViewerInfoRes.from(resumeView));
        }
        return viewerInfoList;
    }

}