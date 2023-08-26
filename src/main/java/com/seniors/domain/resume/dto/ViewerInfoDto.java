package com.seniors.domain.resume.dto;


import com.seniors.domain.resume.entity.Resume;
import com.seniors.domain.resume.entity.ResumeView;
import com.seniors.domain.users.entity.Users;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Setter
public class ViewerInfoDto {

    @Data
    public static class GetViewerInfoRes {

        @Schema(description = "users 기본키")
        private Long userId;

        @Schema(description = "이름")
        private String name;

        @Schema(description = "직종")
        private String occupation;

        @Schema(description = "조회 일자")
        private String date;

        @Schema(description = "사진 URL")
        private String photoUrl;

        @Builder
        private GetViewerInfoRes(ResumeView resumeView) {
            this.userId = resumeView.getUsers().getId();
            this.name = resumeView.getUsers().getNickname();
            this.occupation = resumeView.getResume().getOccupation();
            this.date = dateTransfer(resumeView.getCreatedAt());
            this.photoUrl = resumeView.getUsers().getProfileImageUrl();
        }

        public static GetViewerInfoRes from(ResumeView resumeView) {
            return GetViewerInfoRes.builder()
                    .resumeView(resumeView)
                    .build();
        }

        public String dateTransfer(LocalDateTime localDateTime){
            Long daysDifference = ChronoUnit.DAYS.between(localDateTime.toLocalDate(), LocalDate.now());
            if (daysDifference == 0){
                return "오늘";
            }
            else{
                return daysDifference + "일 전";
            }
        }
    }
}
