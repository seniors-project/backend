package com.side.api.report;

import com.side.api.common.BaseEntity;
import com.side.common.constant.ReportStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Report extends BaseEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint unsigned not null COMMENT '문의 ID'")
    private Long id;

    @Column(columnDefinition = "varchar(18) not null COMMENT '문의 제목'")
    private String title;

    @Column(columnDefinition = "text not null COMMENT '문의 내용'")
    @Lob
    private String content;

    @Column(columnDefinition = "varchar(20) default '접수' not null COMMENT '문의 상태'")
    @Enumerated(EnumType.STRING)
    private ReportStatus status;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    public static Report initReport(String title, String content, User user) {
//        return Report.builder()
//                .title(title).content(content).status(ReportStatus.WAIT)
//                .user(user)
//                .build();
//    }

}
