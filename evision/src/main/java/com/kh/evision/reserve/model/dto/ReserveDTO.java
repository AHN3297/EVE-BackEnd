package com.kh.evision.reserve.model.dto;

import java.sql.Date;

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
public class ReserveDTO {
	
	private Long reserveNo;
	private Long carNo;
	private Long memberNo;
	private String status;
	
	@NotBlank
	private Date reserveDate;
	
	@NotBlank
	private Date rentalStartDate;
	
	@NotBlank
	private Date rentalEndDate;
	private String approveStatus;

}
