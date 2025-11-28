package com.kh.evision.report.model.dto;

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
public class ReportDTO {
	private Long reportNo;
	private Long memberNo;
	private Long boardNo;
	private String reportCategory;
	private String reportTitle;
	private String reportContent;
	private String status;
	private Date reportDate;
	
}
