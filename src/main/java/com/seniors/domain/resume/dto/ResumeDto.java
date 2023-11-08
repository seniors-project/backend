package com.seniors.domain.resume.dto;

import com.seniors.domain.resume.entity.Resume;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Setter
public class ResumeDto {
	@Data
	public static class SaveResumeReq {

		@Schema(description = "소개글")
		private String introduce;

		@NotEmpty(message = "직종은 비워둘 수 없습니다.")
		@Size(max = 30, message = "직종은 30자 이하여야 합니다.")
		@Schema(description = "직종")
		private String occupation;

		@Schema(description = "공개 여부")
		private Boolean isOpened;

		@NotEmpty(message = "이름은 비워둘 수 없습니다.")
		@Size(max = 30, message = "이름은 30자 이하여야 합니다.")
		@Schema(description = "이름")
		private String name;

		@Valid
		private List<CertificateDto.saveCertificateReq> certificateList = new ArrayList<>();

		@Valid
		private List<CareerDto.saveCareerReq> careerList = new ArrayList<>();

		@Valid
		private List<EducationDto.saveEducationReq> educationList = new ArrayList<>();

	}

	@Data
	public static class ModifyResumeReq {

		@Schema(description = "기본키")
		private Long id;

		@Schema(description = "소개글")
		private String introduce;

		@NotEmpty(message = "직종은 비워둘 수 없습니다.")
		@Size(max = 30, message = "직종은 30자 이하여야 합니다.")
		@Schema(description = "직종")
		private String occupation;

		@Schema(description = "공개 여부")
		private Boolean isOpened;

		@NotEmpty(message = "이름은 비워둘 수 없습니다.")
		@Size(max = 30, message = "이름은 30자 이하여야 합니다.")
		@Schema(description = "이름")
		private String name;

		@Valid
		private List<CertificateDto.modifyCertificateReq> certificateList = new ArrayList<>();

		@Valid
		private List<CareerDto.modifyCareerReq> careerList = new ArrayList<>();

		@Valid
		private List<EducationDto.modifyEducationReq> educationList = new ArrayList<>();

	}

	@Data
	public static class GetResumeRes {

		@Schema(description = "기본키")
		private Long id;

		@Schema(description = "소개글")
		private String introduce;

		@Schema(description = "사진 URL")
		private String photoUrl;

		@Schema(description = "직종")
		private String occupation;

		@Schema(description = "공개 여부")
		private Boolean isOpened;

		@Schema(description = "이름")
		private String name;

		@Schema(description = "조회수")
		private Integer viewCount;

		private List<CertificateDto.getCertificateRes> certificates;

		private List<CareerDto.getCareerRes> careers;

		private List<EducationDto.getEducationRes> educations;


		@Builder
		public GetResumeRes(Resume resume) {
			this.id = resume.getId();
			this.introduce = resume.getIntroduce();
			this.photoUrl = resume.getPhotoUrl();
			this.occupation = resume.getOccupation();
			this.isOpened = resume.getIsOpened();
			this.name = resume.getName();
			this.viewCount = resume.getViewCount();
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

		@Schema(description = "기본키")
		private Long id;

		@Schema(description = "소개글")
		private String introduce;

		@Schema(description = "사진 URL")
		private String photoUrl;

		@Schema(description = "직종")
		private String occupation;

		@Schema(description = "공개 여부")
		private Boolean isOpened;

		@Schema(description = "이름")
		private String name;

		@Schema(description = "조회수")
		private Integer viewCount;

		@Schema(description = "유저 기본키")
		private Long userId;


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
			this.viewCount = resume.getViewCount();
			this.certificates = resume.getCertificates().stream().map(CertificateDto.getCertificateQueryDslRes::new).collect(Collectors.toList());
			this.careers = resume.getCareers().stream().map(CareerDto.getCareerQueryDslRes::new).collect(Collectors.toList());
			this.educations = resume.getEducations().stream().map(EducationDto.getEducationByQueryDslRes::new).collect(Collectors.toList());
			this.userId = resume.getUsers().getId();
		}

	}
}