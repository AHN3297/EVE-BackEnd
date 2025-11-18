package com.kh.evision.car.model.vo;

import java.sql.Date;

import lombok.Builder;

@Builder
public class CarVO {
	
	private Long carNo;
	private String carName;
	private String carPlate;
	private int maxPassenger;
	private String color;
	private String status;
	private Date registerDate;
	private String carLocation;
	private String carBrand;

}
