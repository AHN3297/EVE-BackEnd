package com.kh.evision.station.model.vo;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
@AllArgsConstructor
@Builder
public class StationVO {
	private Long stationNo;
	private String stationName;
	private String stationAddress;
	private String stationType;
	private double stationLng;
	private double stationLat;
	private Date registerDate;
	private String status;
	private String delStatus;

}
