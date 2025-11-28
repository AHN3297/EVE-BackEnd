package com.kh.evision.report.model.vo;

import java.sql.Date;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class ReportVO {
	private Long reportNo;
	private Long memberNo;
	private Long boardNo;
	private String reportCategory;
	private String reportTitle;
	private String reportContent;
	private String status;
	private Date reportDate;
}

