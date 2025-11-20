package com.kh.evision.station.model.dto;

import java.sql.Date;

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
public class StationDTO {
	private Long stationNo;
	private String stationName;
	private String stationAddress;
	private String stationType;
	private Date registerDate;
	private String status;
	private Long stationCount;
	private Long useableStation;
}
