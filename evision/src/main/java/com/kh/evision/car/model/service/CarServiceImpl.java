package com.kh.evision.car.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kh.evision.car.model.dao.CarMapper;
import com.kh.evision.car.model.dto.CarCreateDTO;
import com.kh.evision.car.model.dto.CarDTO;
import com.kh.evision.car.model.vo.CarVO;
import com.kh.evision.exception.InvalidParameterException;
import com.kh.evision.exception.custom.car.CarAlreadyReservedException;
import com.kh.evision.exception.custom.car.CarNotAvailableException;
import com.kh.evision.exception.custom.car.CarNotFoundException;
import com.kh.evision.file.FileInfo;
import com.kh.evision.file.FileService;
import com.kh.evision.file.ImgInfo;
import com.kh.evision.file.ImgService;
import com.kh.evision.util.PageInfo;
import com.kh.evision.util.Pagination;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
	
	private final CarMapper carMapper;
	
	private final Pagination pagination;
	private final FileService fileService;
	private final ImgService imgService;
	
	// 차량 등록
	@Override
	public void saveCar(CarCreateDTO car, List<MultipartFile> files) {
		
		CarVO c = null;
		
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
		
		Long carNo = c.getCarNo();
		// 반환이 void이므로 번호를 받아올 수 없음 -> MyBatis selectKey 이용해서 받아오기
		log.info("차량 저장 후 PK 확인 : {}", carNo);
		
		// 파일존재여부 확인
		// 있으면 업로드하고 VO에 담아서 넘기기 / 없으면 그냥 넘기기 -> 어쨌든 VO는 넘겨줘야한다 -> 파일 작업만 if로 구분
		if(files != null && !files.isEmpty()) {
			
			// MultipartFile에 이미지 파일인지 확인하는 메소드가 있음
			for(MultipartFile file : files) {
				
				// 어떤건지 확인하고 -> 이미지나 파일에 맞게 ImgService, FileService 호출
				String fileType = file.getContentType();
				log.info("파일 타입 알려줘 : {}", fileType);
				
				if(fileType != null && fileType.startsWith("image/")) {
					
					log.info("이미지로 판명났음 : {}", file);
					ImgInfo imgInfo = imgService.store(file, carNo); // 이미지 자체를 서버에 저장
					// 돌아오는거 받아서 DB에 파일 정보 저장해야함
					carMapper.saveCarImg(imgInfo);
					log.info("이미지 저장 완료 : {}", imgInfo.getChangeName());
					
				} else {
					
					log.info("파일로 판명났음 : {}", file);
					FileInfo fileInfo = fileService.store(file, carNo); // 파일 자체를 서버에 저장
					carMapper.saveCarFile(fileInfo);
					log.info("파일 저장 완료 : {}", fileInfo.getChangeName());
					
				}
				
			}
			
		}
		
	}
	
	// 차량 목록 조회
	@Override
	public Map<String, Object> findAll(int pageNo) {
		
		Map<String, Object> map = new HashMap();
		List<CarDTO> cars = new ArrayList();
		
		// 예외처리됨?
		log.info("불러와지나요?");
		
		// 페이지 번호 검증 -> 예외처리 해야함(Bad Request)
		if(pageNo < 0) {
			log.info("예외는 여기 들어와야해!");
			throw new InvalidParameterException("유효하지 않은 접근입니다.");
		}
		
		// 페이징처리 고민!
		int count = carMapper.selectTotalCount();
		
		// 조회된 차량이 없을 때
		if(count < 1) {
			
			map.put("pi", null);
			map.put("cars", new ArrayList<>());
			return map;
			
		}
		
		PageInfo pi = pagination.getPageInfo(count, pageNo, 5, 5);
		
		// 요청한 페이지가 최대 페이지 초과했을 때
		if(pageNo > pi.getMaxPage()) {
	        throw new InvalidParameterException("존재하지 않는 페이지입니다.");
	    }
		
//		// 조회된게 없을수도 있는 예외처리? -> 만들어야함! -> 예외처리 대신 빈 값 반환
//		if(count < 1) {
//			throw new RuntimeException("조회된 내용이 없습니다.");
//		} else {
		
		RowBounds rb = new RowBounds((pageNo - 1) * 5, 5);
		cars = carMapper.findAll(rb);
		
		map.put("pi", pi);
		map.put("cars", cars);
		
		return map;
		
	}
	
	// 차량 정보 수정
	@Override
	public CarDTO updateCar(Long carNo, CarDTO car, List<MultipartFile> files) {
		
		// 차량 있는지
		CarDTO existingCar = findByCarNo(carNo);
		
		// 예약중인 차량 예외처리
		if("Y".equals(existingCar.getRentalStatus())) {
			throw new CarAlreadyReservedException("예약 중인 차량 정보는 수정할 수 없습니다");
		}
		
		existingCar.setCarBrand(car.getCarBrand());
		existingCar.setCarLocation(car.getCarLocation());
		existingCar.setCarName(car.getCarName());
		existingCar.setCarPlate(car.getCarPlate());
		existingCar.setColor(car.getColor());
		existingCar.setMaxPassenger(car.getMaxPassenger());
		
		if(files != null && !files.isEmpty()) {
			
	        for(MultipartFile file : files) {
	        	
	            String fileType = file.getContentType();
	            
	            if(fileType != null && fileType.startsWith("image/")) {
	            	
	                ImgInfo imgInfo = imgService.store(file, carNo);
	                carMapper.saveCarImg(imgInfo);
	                
	            } else {
	            	
	                FileInfo fileInfo = fileService.store(file, carNo);
	                carMapper.saveCarFile(fileInfo);
	                
	            }
	            
	        }
	        
	    }
		
		carMapper.updateCar(existingCar);
		
		return existingCar;
		
	}
	
	// 차량 상세 조회
	@Override
	public CarDTO findByCarNo(Long carNo) {
		log.info("컨트롤러에서 서비스로 차량 번호 넘어오는지 : {}", carNo);
		
		CarDTO car = carMapper.findByCarNo(carNo);
		
		if(car == null) {
			throw new CarNotFoundException("해당 차량을 찾을 수 없습니다.");
		}
		
		// 삭제된 차량인 경우
		if("N".equals(car.getStatus())) {
			throw new CarNotAvailableException("더이상 사용할 수 없는 차량입니다.");
		}
		
		return car;
	}
	
	// 차량 삭제
	@Override
	public void deleteByCarNo(Long carNo) {
		
		// 있어야 삭제를 허지~
		CarDTO car = findByCarNo(carNo);
		
		// 근데 예약중이면 못지움
		if("Y".equals(car.getRentalStatus())) {
			throw new CarAlreadyReservedException("예약중인 차량은 삭젲할 수 없습니다.");
		}
		
		carMapper.deleteByCarNo(carNo);
		
	}
	
	// 차량 검색

}
