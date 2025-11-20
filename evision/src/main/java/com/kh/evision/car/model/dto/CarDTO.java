package com.kh.evision.car.model.dto;

import java.sql.Date;
import java.util.List;

import com.kh.evision.file.FileInfo;
import com.kh.evision.file.ImgInfo;

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
public class CarDTO {
	
	private Long carNo;
	
	@NotBlank
	private String carName;
	
	private String carPlate;
	private int maxPassenger;
	private String color;
	private String status;
	private Date registerDate;
	private String carLocation;
	private String carBrand;
	
	private List<ImgInfo> imgs;
	private List<FileInfo> files;

}
