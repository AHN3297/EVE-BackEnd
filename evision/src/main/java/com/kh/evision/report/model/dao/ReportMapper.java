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
	
	// 신고 존재 여부 확인
	ReportDTO findByReportNo(Long reportNo);
	
	// 중복 신고 확인 (같은 회원이 같은 게시글을 신고했는지)
	int existsByMemberNoAndBoardNo(Long memberNo, Long boardNo);
	
}

