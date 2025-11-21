package com.kh.evision.station.model.vo;

import java.sql.Date;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewVO {
    private Long reviewNo;
    private Long stationNo;
    private String reviewTitle;
    private String reviewContent;
    private String status;
    private LocalDateTime reviewDate;
    private Long memberNo;
}

