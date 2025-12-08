package com.kh.evision.notice.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeFileVO {
    private Long fileId;
    private Long noticeNo;
    private String originName;
    private String changeName;
    private String status;
}