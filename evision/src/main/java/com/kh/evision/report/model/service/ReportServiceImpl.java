package com.kh.evision.report.model.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.evision.exception.InvalidParameterException;
import com.kh.evision.exception.custom.report.DuplicateReportException;
import com.kh.evision.exception.custom.report.InvalidReportStatusException;
import com.kh.evision.exception.custom.report.ReportNotFoundException;
import com.kh.evision.report.model.dao.ReportMapper;
import com.kh.evision.report.model.dto.ReportDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReportServiceImpl implements ReportService {
	private final ReportMapper mapper;
	
	// 유효한 신고 상태 목록
	private static final List<String> VALID_STATUSES = Arrays.asList("접수", "처리중", "완료", "반려");
	
	@Override
	public int save(ReportDTO report) {
		validateReportDTO(report);
		
		log.info("신고/문의 등록 - 카테고리: {}, 게시글번호: {}", report.getReportCategory(), report.getBoardNo());
		
		// 중복 신고 확인 (문의가 아닌 경우, boardNo가 있을 때만)
		if (!"INQUIRY".equals(report.getReportCategory()) && report.getBoardNo() != null) {
			if (mapper.existsByMemberNoAndBoardNo(report.getMemberNo(), report.getBoardNo()) > 0) {
				throw new DuplicateReportException(report.getBoardNo());
			}
		}
		
		return mapper.save(report);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ReportDTO> findAll() {
		return mapper.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public ReportDTO findByKeyword(String keyword) {
		if (keyword == null || keyword.trim().isEmpty()) {
			throw new InvalidParameterException("검색어를 입력해주세요.");
		}
		ReportDTO report = mapper.findByKeyword(keyword.trim());
		if (report == null) {
			throw new ReportNotFoundException("검색 결과가 없습니다. 검색어: " + keyword);
		}
		return report;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ReportDTO> findMyReports(Long memberNo) {
		if (memberNo == null) {
			throw new InvalidParameterException("회원 번호가 필요합니다.");
		}
		return mapper.findMyReports(memberNo);
	}
	
	@Override
	public int updateStatus(ReportDTO report) {
		if (report == null || report.getReportNo() == null) {
			throw new InvalidParameterException("신고 번호가 필요합니다.");
		}
		if (report.getStatus() == null || report.getStatus().trim().isEmpty()) {
			throw new InvalidParameterException("변경할 상태를 입력해주세요.");
		}
		
		// 유효한 상태인지 확인
		if (!VALID_STATUSES.contains(report.getStatus())) {
			throw new InvalidReportStatusException("유효하지 않은 상태입니다. 허용된 상태: " + VALID_STATUSES);
		}
		
		// 신고 존재 여부 확인
		ReportDTO existingReport = mapper.findByReportNo(report.getReportNo());
		if (existingReport == null) {
			throw new ReportNotFoundException(report.getReportNo());
		}
		
		int result = mapper.updateStatus(report);
		if (result == 0) {
			throw new ReportNotFoundException("상태 변경에 실패했습니다.");
		}
		return result;
	}
	
	@Override
	public int deleteReport(Long reportNo) {
		if (reportNo == null) {
			throw new InvalidParameterException("신고 번호가 필요합니다.");
		}
		
		// 신고 존재 여부 확인
		ReportDTO existingReport = mapper.findByReportNo(reportNo);
		if (existingReport == null) {
			throw new ReportNotFoundException(reportNo);
		}
		
		int result = mapper.deleteReport(reportNo);
		if (result == 0) {
			throw new ReportNotFoundException("신고 삭제에 실패했습니다.");
		}
		return result;
	}
	
	// 유효성 검증
	
	private void validateReportDTO(ReportDTO report) {
		if (report == null) {
			throw new InvalidParameterException("신고 정보가 필요합니다.");
		}
		if (report.getReportCategory() == null || report.getReportCategory().trim().isEmpty()) {
			throw new InvalidParameterException("카테고리는 필수 항목입니다.");
		}
		// 문의(INQUIRY)가 아닌 경우에만 게시글 번호 필수
		if (!"INQUIRY".equals(report.getReportCategory()) && report.getBoardNo() == null) {
			throw new InvalidParameterException("게시글 번호가 필요합니다.");
		}
		if (report.getReportTitle() == null || report.getReportTitle().trim().isEmpty()) {
			throw new InvalidParameterException("제목은 필수 항목입니다.");
		}
		if (report.getReportContent() == null || report.getReportContent().trim().isEmpty()) {
			throw new InvalidParameterException("내용은 필수 항목입니다.");
		}
	}
}
