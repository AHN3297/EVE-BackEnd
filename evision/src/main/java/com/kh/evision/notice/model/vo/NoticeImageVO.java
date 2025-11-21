package com.kh.evision.notice.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeImageVO {
    private Long imgId;
    private Long noticeNo;
    private String originName;
    private String changeName;
    private char status;
}
