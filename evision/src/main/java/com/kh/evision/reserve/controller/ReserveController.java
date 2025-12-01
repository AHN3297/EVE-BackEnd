package com.kh.evision.reserve.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.evision.reserve.model.dto.ReserveDTO;
import com.kh.evision.reserve.model.service.ReserveService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Validated
@RequestMapping("/reserve")
@RequiredArgsConstructor
public class ReserveController {
	
	// 서비스를 필드로 등록
	private final ReserveService reserveService;
	
	// 차량 예약 -> Post
	@PostMapping
	public ResponseEntity<?> reserveCar(ReserveDTO reserve
			// , @AuthenticationPrincipal CustomUserDetails userDetails
			) {
		// 차량, 사용자, 예약신청일, 사용시작일, 반납예정일
		// 예약 DTO에 담겨있음
		
		log.info("차량 예약 메소드 호출 시도");
		
		log.info("FE에서 넘어오는 예약 정보 : {}", reserve);
		
		reserveService.reserveCar(reserve);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
		
	}
	
	// 차량 예약 내역 조회(사용자)
	@GetMapping("{memberNo}")
	public ResponseEntity<Map<String, Object>> findAllUserReserve(
			// @AuthenticationPrincipal CustomUserDetails userDetails
			) {
		
		log.info("사용자용 차량 예약 내역 조회기능 호출");
		Map<String, Object> map = reserveService.findAllUserReserve();
		// 예약 내역 조회 결과 + 페이징 객체
		
		return ResponseEntity.ok(map);
		
	}
	
	// 차량 예약 내역 조회(관리자)
	@GetMapping("/operator/reserve-manage")
	public ResponseEntity<Map<String, Object>> findAllReserve(
			// @AuthenticationPrincipal CustomUserDetails userDetails
			) {
		
		log.info("관리자용 차량 예약 내역 조회기능 호출");
		
		Map<String, Object> map = reserveService.findAllReserve();
		
		return ResponseEntity.ok(map);
		
	}
	
}
