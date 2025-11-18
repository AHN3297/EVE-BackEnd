package com.kh.evision.car.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kh.evision.car.model.dto.CarDTO;
import com.kh.evision.car.model.vo.CarVO;
import com.kh.evision.file.FileService;
import com.kh.evision.file.ImgService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
	
	private final FileService fileService;
	private final ImgService imgService;
	
	// 차량 등록
	@Override
	public void saveCar(CarDTO car, List<MultipartFile> file) {
		
		CarVO c = null;
		
		// 파일존재여부 확인
		// 있으면 업로드하고 VO에 담아서 넘기기 / 없으면 그냥 넘기기
		if(file != null && !file.isEmpty()) {
			
			// 파일이 여러개 있을 수 있음, 리스트에서 꺼내서
			// 있는지 없는지 검증
			// 있으면 이름변경, 업로드 -> 공통모듈 업로드 메소드 호출
			// 이미지면 이미지 서비스의 메소드 / 이미지가 아니면 파일 서비스의 메소드 호출
			
			/*
			 * 이미 만들어둔 이미지 서비스 클래스에서 이미지 확장자를 검증하는 방법을 재사용할 수 없을까?
			 * -> 고민중!
			 * 
			 */
			
		}
		
	}
	
	// 차량 목록 조회
	
	// 차량 정보 수정
	
	// 차량 상세 조회
	
	// 차량 삭제
	
	// 차량 검색

}
