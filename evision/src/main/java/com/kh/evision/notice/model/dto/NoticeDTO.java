package com.kh.evision.notice.model.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDTO {
    private Long noticeNo;
    private String noticeTitle;
    private String noticeContent;
    private LocalDateTime createDate;
    private Long memberNo;
    private char status;
    private List<String> imageUrls;
    private List<String> originalFileNames;
    private String thumbnailUrl;  // 대표 이미지 (1개)
    private List<String> fileUrls; // 기타 첨부 파일들
}