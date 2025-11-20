package com.kh.evision.station.model.vo;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data                // getter, setter, toString 등 생성
@NoArgsConstructor   // 기본 생성자 - Jackson 필수!
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
