package com.kh.evision.station.model.vo;

import java.sql.Date;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Value;


@Builder
@Value
public class ReviewVO {
    private Long reviewNo;
    private Long stationNo;
    private String reviewTitle;
    private String reviewContent;
    private String status;
    private LocalDateTime reviewDate;
    private Long memberNo;
}

