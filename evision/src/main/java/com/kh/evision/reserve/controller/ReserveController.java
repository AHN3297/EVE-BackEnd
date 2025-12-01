package com.kh.evision.reserve.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.evision.reserve.model.dto.ReserveDTO;
import com.kh.evision.reserve.model.service.ReserveService;

import jakarta.validation.constraints.Min;
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
	@GetMapping("/{memberNo}")
	public ResponseEntity<Map<String, Object>> findAllUserReserve(@RequestParam(name="pageNo", defaultValue="0") int pageNo
			// @AuthenticationPrincipal CustomUserDetails userDetails
			) {
		
		log.info("사용자용 차량 예약 내역 조회기능 호출");
		Map<String, Object> map = reserveService.findAllUserReserve(pageNo);
		// 예약 내역 조회 결과 + 페이징 객체
		
		return ResponseEntity.ok(map);
		
	}
	
	// 차량 예약 내역 조회(운영자)
	@GetMapping("/operator/reserve-manage")
	public ResponseEntity<Map<String, Object>> findAllReserve(@RequestParam(name="pageNo", defaultValue="0") int pageNo
			// @AuthenticationPrincipal CustomUserDetails userDetails
			) {
		
		log.info("운영자용 차량 예약 내역 조회기능 호출");
		
		Map<String, Object> map = reserveService.findAllReserve(pageNo);
		
		return ResponseEntity.ok(map);
		
	}
	
	// 차량 예약 내역 상세조회
	@GetMapping("/details/{reserveNo}")
	public ResponseEntity<ReserveDTO> findByReserveNo(@PathVariable(name="reserveNo") @Min(value=1, message="올바른 접근 경로가 아닙니다.") Long reserveNo) {
		
		log.info("FE에서 넘어오는 예약 번호 : {}", reserveNo);
		
		ReserveDTO reserve = reserveService.findByReserveNo(reserveNo);
		
		log.info("예약 상세 조회 끝 : {}", reserve);
		
		return null; // ResponseEntity.ok(reserve);
		
	}
	
}
