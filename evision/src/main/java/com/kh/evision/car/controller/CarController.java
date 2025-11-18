package com.kh.evision.car.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kh.evision.car.model.dto.CarDTO;
import com.kh.evision.car.model.service.CarService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {
	
	private final CarService carService;
	
	// 차량 등록 -> 관리자/운영자용 기능, 이미지첨부, 파일첨부
	@PostMapping
	public ResponseEntity<?> saveCar(@Valid CarDTO car, @RequestParam(name="file", required=false) List<MultipartFile> file) {
		// 파일 + 이미지 두개 올수있음.. 이거 다시 생각해야함! -> 리스트로 받음
		
		log.info("FE에서 넘어오는 차량 정보 : {}", car);
		
		carService.saveCar(car, file);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
		
	}
	
	// 차량 목록 조회
	@GetMapping
	public ResponseEntity<List<CarDTO>> findAll(@RequestParam(name="page", defaultValue="0") int pageNo) {
		
		List<CarDTO> cars = carService.findAll(pageNo);
		return ResponseEntity.ok(cars);
		
	}
	
	// 차량 정보 수정
	
	// 차량 상세 조회
	
	// 차량 삭제
	
	// 차량 검색

}
