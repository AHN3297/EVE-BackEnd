package com.kh.evision.car.model.vo;

import java.sql.Date;

import lombok.Builder;
import lombok.Value;

@Value // 불변객체 생성용
@Builder // 생성 편하게 하려고
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
	private String rentalStatus;

}
