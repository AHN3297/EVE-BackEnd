package com.kh.evision.reserve.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Validated
@RequestMapping("/reserve")
@RequiredArgsConstructor
public class ReserveController {
	
	// 서비스를 필드로 등록
	
	// 차량 예약 -> Post
	@PostMapping
	public ResponseEntity<?> reserveCar() {
		// 차량, 사용자, 예약신청일, 사용시작일, 반납예정일
		// 예약 DTO
		
		log.info("차량 예약 메소드 호출 시도");
		
		log.info("FE에서 넘어오는 차량 정보 : {}", carNo);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
		
	}

}
