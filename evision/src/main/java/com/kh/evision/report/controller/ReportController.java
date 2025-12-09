package com.kh.evision.report.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.evision.auth.model.vo.CustomUserDetails;
import com.kh.evision.report.model.dto.ReportDTO;
import com.kh.evision.report.model.service.ReportService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {
	private final ReportService reportService;
	
	// 신고 등록
	@PostMapping
	public ResponseEntity<String> save(@RequestBody ReportDTO report,
								  @AuthenticationPrincipal CustomUserDetails userDetails){
		report.setMemberNo(Long.parseLong(userDetails.getUsername()));
		log.info("신고 등록 요청 - 게시글 번호: {}, 회원 번호: {}", report.getBoardNo(), report.getMemberNo());
		reportService.save(report);
		return ResponseEntity.status(HttpStatus.CREATED).body("신고 등록 성공");
	}
	
	// 관리자(admin) - 전체 신고 목록 조회
	@GetMapping
	public ResponseEntity<List<ReportDTO>> findAll(){
		log.info("전체 신고 목록 조회 요청");
		return ResponseEntity.ok(reportService.findAll());
	}
	
	// 키워드로 검색
	@GetMapping(params = "keyword")
	public ResponseEntity<ReportDTO> findByKeyword(@RequestParam(name="keyword") String keyword) {
		log.info("신고 검색 요청 - 키워드: {}", keyword);
		return ResponseEntity.ok(reportService.findByKeyword(keyword));
	}
	
	// 사용자(user) - 내 신고 목록 조회
	@GetMapping("/my")
	public ResponseEntity<List<ReportDTO>> findMyReports(@AuthenticationPrincipal CustomUserDetails userDetails) {
		Long memberNo = Long.parseLong(userDetails.getUsername());
		log.info("내 신고 목록 조회 요청 - 회원 번호: {}", memberNo);
		return ResponseEntity.ok(reportService.findMyReports(memberNo));
	}
	
	// 상태 변경
	@PutMapping
	public ResponseEntity<String> updateStatus(@RequestBody ReportDTO report) {
		log.info("신고 상태 변경 요청 - 신고 번호: {}, 변경 상태: {}", report.getReportNo(), report.getStatus());
		reportService.updateStatus(report);
		return ResponseEntity.ok("상태 변경 성공");
	}

	// 신고 삭제
	@DeleteMapping(params="reportNo")
	public ResponseEntity<String> deleteReport(@RequestParam(name="reportNo") Long reportNo){
		log.info("신고 삭제 요청 - 신고 번호: {}", reportNo);
		reportService.deleteReport(reportNo);
		return ResponseEntity.ok("신고 삭제 성공");
	}
	
}