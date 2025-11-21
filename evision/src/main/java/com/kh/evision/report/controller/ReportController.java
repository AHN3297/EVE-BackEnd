package com.kh.evision.report.controller;

//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

//	@PostMapping
//	public ResponseEntity<?> save(@RequestBody ReportDTO report,
//								  @AuthenticationPrincipal CustomUserDetails userDetails){
//		Report r = reportService.save(report, userDetails);
//		return ResponseEntity.status(HttpStatus.CREATED).body(r);
//	}
//	
//	// 사용자
//	@GetMapping(params = "memberNo")
//	public ResponseEntity<List<ReportDTO>> findMyReports(@AuthenticationPrincipal CustomUserDetails userDetails) {
//	    log.info("내 신고/문의 목록 조회 - 사용자: {}", userDetails.getUsername());
//	    return ResponseEntity.ok(reportService.findByMemberNo(userDetails.getMemberNo()));
//	}
//	
//	// 관리자
//	@GetMapping
//	public ResponseEntity<List<ReportDTO>> findAll(){
//		return ResponseEntity.ok(reportService.findAll());
//	}
//	
//	@GetMapping(params = "reportNo")
//	public ResponseEntity<ReportDTO> findByReportNo(@RequestParam(name="reportNo") Long reportNo) {
//		return ResponseEntity.ok(reportService.findByReportNo(reportNo));
//		
//	}
}