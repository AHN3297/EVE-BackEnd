package com.kh.evision.member.model.vo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LicenseVO {
	private long licenseId;
	private long memberNo;
	private long licenseNo;
	private Date renewDate;
	private String issuingAgency;
	private String LicenseClass;
}
