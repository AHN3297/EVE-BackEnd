package com.kh.evision.car.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kh.evision.car.model.dto.CarDTO;
import com.kh.evision.car.model.vo.CarVO;

@Mapper
public interface CarMapper {
	
	// 차량 등록
	void saveCar(CarVO car);
	
	// 차량 전체 개수 조회
	int selectTotalCount();
	
	// 차량 목록 조회
	List<CarDTO> findAll();
	
	// 차량 정보 수정
	CarDTO updateCar(CarDTO car);
	
	// 차량 상세 조회
	CarDTO findByCarNo(Long carNo);
	
	// 차량 삭제
	void deleteByCarNo(Long carNo);
	
	// 차량 검색

}
