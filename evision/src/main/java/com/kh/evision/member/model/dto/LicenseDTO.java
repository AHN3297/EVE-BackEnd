package com.kh.evision.member.model.dto;

import java.time.LocalDate;

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
public class LicenseDTO {
	private Long licenseNo;
    private LocalDate renewDate;
    private String issuingAgency;
    private String licenseClass;

}
