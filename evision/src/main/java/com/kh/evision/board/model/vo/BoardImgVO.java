package com.kh.evision.board.model.vo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BoardImgVO {
    private Long imgId;
    private Long imgNo;
    private String originName;
    private String changeName;
    private String status;

}
