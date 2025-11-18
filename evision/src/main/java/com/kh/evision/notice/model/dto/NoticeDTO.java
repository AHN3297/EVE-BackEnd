package com.kh.evision.notice.model.dto;

import java.time.LocalDateTime;
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
}