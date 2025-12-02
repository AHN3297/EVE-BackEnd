package com.kh.evision.station.model.dto;

import java.sql.Date;

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
public class StationDTO {
	private String keyword;
	private Long stationNo;
	private String stationName;
	private String stationAddress;
	private double stationLng;
	private double stationLat;
	private String stationType;
	private Date registerDate;
	private String status;
	private String delStatus;
}
