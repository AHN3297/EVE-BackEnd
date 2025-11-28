package com.kh.evision.report.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kh.evision.report.model.dto.ReportDTO;

@Mapper
public interface ReportMapper {
	
	int save(ReportDTO report);
	List<ReportDTO> findAll();
	ReportDTO findByKeyword(String keyword);
	List<ReportDTO> findMyReports(Long memberNo);
	int updateStatus(ReportDTO report);
	int deleteReport(Long reportNo);
	
}

