package com.kh.evision.station.model.vo;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@AllArgsConstructor
@Builder
//@Value
public class StationVO {
	private Long stationNo;
	private String stationName;
	private String stationAddress;
	private String stationType;
	private Long stationLng;
	private Long stationLat;
	private Date registerDate;
	private String status;
	private String delStatus;

}
