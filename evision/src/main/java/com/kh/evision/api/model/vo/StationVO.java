package com.kh.evision.api.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StationVO {
    private int stationNo;
    private String stationName;
    private String stationAddress;
    private String stationType;
    private double stationLng;
    private double stationLat;
    private String status;
    private int stationCount;
    private int useableStation;
}