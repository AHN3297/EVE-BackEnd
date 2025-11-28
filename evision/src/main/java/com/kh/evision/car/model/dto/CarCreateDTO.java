package com.kh.evision.car.model.dto;

import jakarta.validation.constraints.NotBlank;
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
public class CarCreateDTO {
	// 차량 등록 / 수정용 DTO
	
	// private Long carNo;
	
	@NotBlank
	private String carName;
	
	private String carPlate;
	private int maxPassenger;
	private String color;
	// private String status;
	// private Date registerDate;
	private String carLocation;
	private String carBrand;

}
