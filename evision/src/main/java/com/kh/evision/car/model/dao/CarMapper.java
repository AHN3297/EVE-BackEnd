package com.kh.evision.car.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kh.evision.car.model.dto.CarDTO;
import com.kh.evision.car.model.vo.CarVO;

@Mapper
public interface CarMapper {
	
	// 차량 등록
	void saveCar(CarVO car);
	
	// 차량 목록 조회
	List<CarDTO> findAll();
	
	// 차량 정보 수정
	
	// 차량 상세 조회
	
	// 차량 삭제
	
	// 차량 검색

}
