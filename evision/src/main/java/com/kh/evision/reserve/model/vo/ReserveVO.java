package com.kh.evision.reserve.model.vo;

import java.sql.Date;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ReserveVO {
	
	private Long reserveNo;
	private Long carNo;
	private Long memberNo;
	private String status;
	private Date reserveDate;
	private Date rentalStartDate;
	private Date rentalEndDate;
	private String approveStatus;

}
