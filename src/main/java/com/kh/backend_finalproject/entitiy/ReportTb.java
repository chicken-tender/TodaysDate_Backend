package com.kh.backend_finalproject.entitiy;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
public class ReportTb {
    @Id
    @GeneratedValue
    @Column(name = "report_num")
    private Long id;

    @Column(nullable = false, length = 2000)
    private String content; // 신고 내용

    private LocalDateTime reportDate; // 신고 날짜

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "reporter_fk")
    private UserTb reporter; // 신고자

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "reported_fk")
    private UserTb reported; // 신고받은 사람
}
