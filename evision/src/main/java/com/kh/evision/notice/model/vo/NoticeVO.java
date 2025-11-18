package com.kh.evision.notice.model.vo;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeVO {
    private Long noticeNo;
    private String noticeTitle;
    private String noticeContent;
    private LocalDateTime createDate;
    private Long memberNo;
    private char status;
}