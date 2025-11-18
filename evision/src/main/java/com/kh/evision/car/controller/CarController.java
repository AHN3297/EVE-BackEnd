package com.kh.evision.car.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.evision.car.model.service.CarService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {
	
	private final CarService carService;
	
	// 차량 등록
	
	// 차량 목록 조회
	
	// 차량 정보 수정
	
	// 차량 상세 조회
	
	// 차량 삭제
	
	// 차량 검색

}
