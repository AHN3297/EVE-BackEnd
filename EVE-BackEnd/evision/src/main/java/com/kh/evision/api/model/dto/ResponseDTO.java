package com.kh.evision.api.model.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true) 
public class ResponseDTO {
    private int stationNo;
    @JsonProperty("statNm")
    private String stationName;
    @JsonProperty("addr")
    private String stationAddress;
    @JsonProperty("chgerType")
    private String stationType;
    
    @JsonProperty("lng")
    private double stationLng;
    @JsonProperty("lat")
    private double stationLat;
    @JsonProperty("stat")
    private String status;
    // 상태 숫자 값을 문자로 매핑하는 메소드
    public void setStatus(String stat) {
        switch (stat) {
            case "1":
                this.status = "통신이상";
                break;
            case "2":
                this.status = "충전대기";
                break;
            case "3":
                this.status = "충전중";
                break;
            case "4":
                this.status = "운영중지";
                break;
            case "5":
                this.status = "점검중";
                break;
            case "9":
                this.status = "상태미확인";
                break;
            default:
                this.status = "알 수 없음";  // 기본값 처리
                break;
        }
    }
}
