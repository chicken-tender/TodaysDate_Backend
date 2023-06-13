package com.kh.backend_finalproject.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter @AllArgsConstructor
public class ChatbotDto {
    private Long inquiryNum;
    private Long userNum;
    private String inquiryContent;
    private LocalDateTime inquiryDate;
    private String email;
    private String inquiryStatus;
}
