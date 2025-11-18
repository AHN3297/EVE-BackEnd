package com.kh.evision.car.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kh.evision.car.model.dto.CarDTO;

public interface CarService {
	
	// 차량 등록
	void saveCar(CarDTO car, List<MultipartFile> file);
	
	// 차량 목록 조회
	List<CarDTO> findAll(int pageNo);
	
	// 차량 정보 수정
	
	// 차량 상세 조회
	
	// 차량 삭제
	
	// 차량 검색

}
