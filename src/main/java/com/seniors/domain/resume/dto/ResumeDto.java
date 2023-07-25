package com.seniors.domain.resume.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import com.seniors.domain.comment.entity.Comment;
import com.seniors.domain.post.entity.Post;
import com.seniors.domain.resume.entity.Career;
import com.seniors.domain.resume.entity.Certificate;
import com.seniors.domain.resume.entity.Education;
import com.seniors.domain.resume.entity.Resume;
import com.seniors.domain.users.dto.UsersDto.GetPostUserRes;
import com.seniors.domain.users.entity.Users;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.seniors.domain.comment.dto.CommentDto.GetCommentRes;
import static java.lang.Boolean.FALSE;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Setter
public class ResumeDto {
	@Data
	@Setter
	public static class SaveResumeReq {

		private String introduce;

		@NotEmpty(message = "직종은 비워둘 수 없습니다.")
		@Size(max = 30, message = "직종은 30자 이하여야 합니다.")
		private String occupation;

		private Boolean isOpened;

		@NotEmpty(message = "이름은 비워둘 수 없습니다.")
		@Size(max = 30, message = "이름은 30자 이하여야 합니다.")
		private String name;

		private MultipartFile image;

		@Valid
		private List<CertificateDto.saveCertificateReq> certificateList = new ArrayList<>();

		@Valid
		private List<CareerDto.saveCareerReq> careerList = new ArrayList<>();

		@Valid
		private List<EducationDto.saveEducationReq> educationList = new ArrayList<>();

	}

	@Data
	public static class GetResumeRes {

		private Long id;

		private String introduce;

		private String photoUrl;

		private String occupation;

		private Boolean isOpened;

		private String name;

		private List<CertificateDto.getCertificateRes> certificates;

		private List<CareerDto.getCareerRes> careers;

		private List<EducationDto.getEducationRes> educations;


		@Builder
		private GetResumeRes(Resume resume) {
			this.id = resume.getId();
			this.introduce = resume.getIntroduce();
			this.photoUrl = resume.getPhotoUrl();
			this.occupation = resume.getOccupation();
			this.isOpened = resume.getIsOpened();
			this.name = resume.getName();
			this.certificates = resume.getCertificates().stream().map(CertificateDto.getCertificateRes::from)
					.collect(Collectors.toList());
			this.careers = resume.getCareers().stream().map(CareerDto.getCareerRes::from)
					.collect(Collectors.toList());
			this.educations = resume.getEducations().stream().map(EducationDto.getEducationRes::from)
					.collect(Collectors.toList());
		}

		public static GetResumeRes from(Resume resume) {
			return GetResumeRes.builder()
					.resume(resume)
					.build();
		}
	}

	@Data
	@NoArgsConstructor
	@Getter
	@Setter
	public static class GetResumeByQueryDslRes {

		private Long id;
		private String introduce;

		private String photoUrl;

		private String occupation;

		private Boolean isOpened;

		private String name;

		private List<CertificateDto.getCertificateQueryDslRes> certificates;

		private List<CareerDto.getCareerQueryDslRes> careers;

		private List<EducationDto.getEducationByQueryDslRes> educations;


		public GetResumeByQueryDslRes(Resume resume) {
			this.id = resume.getId();
			this.introduce = resume.getIntroduce();
			this.photoUrl = resume.getPhotoUrl();
			this.occupation = resume.getOccupation();
			this.isOpened = resume.getIsOpened();
			this.name = resume.getName();
			this.certificates = resume.getCertificates().stream().map(CertificateDto.getCertificateQueryDslRes::new).collect(Collectors.toList());
			this.careers = resume.getCareers().stream().map(CareerDto.getCareerQueryDslRes::new).collect(Collectors.toList());
			this.educations = resume.getEducations().stream().map(EducationDto.getEducationByQueryDslRes::new).collect(Collectors.toList());
		}

	}
}