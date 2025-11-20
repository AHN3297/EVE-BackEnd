package com.kh.evision.car.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kh.evision.car.model.dao.CarMapper;
import com.kh.evision.car.model.dto.CarDTO;
import com.kh.evision.car.model.vo.CarVO;
import com.kh.evision.exception.InvalidParameterException;
import com.kh.evision.file.FileService;
import com.kh.evision.file.ImgService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
	
	private final CarMapper carMapper;
	
	private final FileService fileService;
	private final ImgService imgService;
	
	// 차량 등록
	@Override
	public void saveCar(CarDTO car, List<MultipartFile> files) {
		
		CarVO c = null;
		
		// 파일존재여부 확인
		// 있으면 업로드하고 VO에 담아서 넘기기 / 없으면 그냥 넘기기 -> 어쨌든 VO는 넘겨줘야한다 -> 파일 작업만 if로 구분
		if(files != null && !files.isEmpty()) {
			
			// 파일이 여러개 있을 수 있음, 리스트에서 꺼내서
			// 있는지 없는지 검증
			// 있으면 이름변경, 업로드 -> 공통모듈 업로드 메소드 호출
			// 이미지면 이미지 서비스의 메소드 / 이미지가 아니면 파일 서비스의 메소드 호출
			
			/*
			 * 이미 만들어둔 이미지 서비스 클래스에서 이미지 확장자를 검증하는 방법을 재사용할 수 없을까?
			 * -> 고민중!
			 * 
			 */
			
			// 일단 한개씩 꺼내보기
			MultipartFile file = files.get(1);
			
			// 하나 이미지로 저장 시도
			imgService.store(file);
			
		}
		
		// 어쨌든 차량 정보는 넘겨서 저장시켜야함
		c = CarVO.builder()
				.carName(car.getCarName())
				.carPlate(car.getCarPlate())
				.maxPassenger(car.getMaxPassenger())
				.color(car.getColor())
				.carLocation(car.getCarLocation())
				.carBrand(car.getCarBrand())
				.build();
		
		carMapper.saveCar(c);
		
	}
	
	// 차량 목록 조회
	@Override
	public List<CarDTO> findAll(Long pageNo) {
		
		// 예외처리됨?
		log.info("불러와지나요?");
		
		// 페이지 번호 검증 -> 예외처리 해야함(Bad Request)
		if(pageNo < 0) {
			log.info("예외는 여기 들어와야해!");
			throw new InvalidParameterException("유효하지 않은 접근입니다.");
		}
		
		// 페이징처리 고민!
		// int count carMapper.
		
		return carMapper.findAll();
		
	}
	
	// 차량 정보 수정
	@Override
	public CarDTO updateCar(Long carNo, CarDTO car, List<MultipartFile> files) {
		
		// 파일이 없었다면 새 파일 첨부
		// 파일 수정되면 기존 파일은 삭제하고 새 파일 추가
		
		if(files != null && !files.isEmpty()) {
			
			fileService.store(null);
			
		}
		
		// 차근차근 해야함
		// 이미지 업로드
		// 파일 업로드
		carMapper.updateCar(car);
		
		// 둘 다 성공해야 리턴
		
		return car;
		
	}
	
	// 차량 상세 조회
	@Override
	public CarDTO findByCarNo(Long carNo) {
		return carMapper.findByCarNo(carNo);
	}
	
	// 차량 삭제
	@Override
	public void deleteByCarNo(Long carNo) {
		carMapper.deleteByCarNo(carNo);
	}
	
	// 차량 검색

}
