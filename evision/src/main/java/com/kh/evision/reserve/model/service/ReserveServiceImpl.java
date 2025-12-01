package com.kh.evision.reserve.model.service;

import org.springframework.stereotype.Service;

import com.kh.evision.reserve.model.dao.ReserveMapper;
import com.kh.evision.reserve.model.dto.ReserveDTO;
import com.kh.evision.reserve.model.vo.ReserveVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReserveServiceImpl implements ReserveService {
	
	private final ReserveMapper reserveMapper;
	
	@Override
	public void reserveCar(ReserveDTO reserve) {
		
		log.info("컨트롤러에서 서비스는 오나요?");
		
		// 날짜 형식 생각해야함
		
		// 예약 정보 넘겨서 저장시키기
		ReserveVO r = null;
		
		r = ReserveVO.builder()
					 .memberNo(reserve.getMemberNo())
					 .carNo(reserve.getCarNo())
					 .reserveDate(reserve.getReserveDate())
					 .rentalStartDate(reserve.getRentalStartDate())
					 .rentalEndDate(reserve.getRentalEndDate())
					 .build();
		
		reserveMapper.reserveCar(r);
		
		Long reserveNo = r.getReserveNo();
		log.info("예약 등록 후 PK 확인 : {}", reserveNo);
		
	}
	
	// 차량 예약 내역 조회(사용자/관리자)

}
