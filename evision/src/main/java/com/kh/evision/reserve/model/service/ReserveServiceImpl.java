package com.kh.evision.reserve.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.evision.reserve.model.dao.ReserveMapper;
import com.kh.evision.reserve.model.dto.ReserveDTO;
import com.kh.evision.reserve.model.vo.ReserveVO;
import com.kh.evision.util.PageInfo;
import com.kh.evision.util.Pagination;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ReserveServiceImpl implements ReserveService {
	
	private final ReserveMapper reserveMapper;
	private final Pagination pagination;
	
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
		
		// reserveCarStatus(r.getCarNo());
		// 여기서 하는게 아니고 관리자가 승인하면 변경해줘야함
		
	}
	
	// 차량 예약 시 차량 정보 수정
	@Override
	public void reserveCarStatus(Long carNo) {
		reserveMapper.reserveCarStatus(carNo);
	}

	// 차량 예약 내역 조회(사용자)
	@Override
	public Map<String, Object> findAllUserReserve(int pageNo
			// CustomUserDetails userDetails
			) {
		
		log.info("사용자용 예약내역 조회 메소드 호출");
		
		Map<String, Object> map = new HashMap();
		List<ReserveDTO> reserveList = new ArrayList();
		
		// 로그인한 사용자 검증, 조회 요청한 사용자 검증 -> 임시작성해둔거 고쳐야함!
		// Long memberNo = userDetails.get();
		Long memberNo = 1L;
		
		int count = reserveMapper.selectUserTotalCount();
		PageInfo pi = pagination.getPageInfo(count, pageNo, 5, 5);
		
		// 조회 없을 때의 예외처리 생성 후 차량 조회에도 적용해야함
		if(count < 1) {
			throw new RuntimeException("조회된 내용이 없습니다.");
		} else {
			
			RowBounds rb = new RowBounds((pageNo - 1) * 5, 5);
			reserveList = reserveMapper.findAllUserReserve(rb, memberNo);
			
			map.put("pi", pi);
			map.put("reserveList", reserveList);
			
		}
		
		return map;
		
	}

	// 차량 예약 내역 조회(운영자)
	@Override
	public Map<String, Object> findAllReserve(int pageNo
			// CustomUserDetails userDetails
			) {
		
		log.info("운영자용 예약내역 조회 메소드 호출");
		
		Map<String, Object> map = new HashMap();
		List<ReserveDTO> reserveList = new ArrayList();
		
		// 운영자 권한 검증
		
		int count = reserveMapper.selectTotalCount();
		PageInfo pi = pagination.getPageInfo(count, pageNo, 5, 5);
		
		// 조회 없을 때의 예외처리 생성 후 차량 조회에도 적용해야함
		if(count < 1) {
			throw new RuntimeException("조회된 내용이 없습니다.");
		} else {
			
			RowBounds rb = new RowBounds((pageNo - 1) * 5, 5);
			reserveList = reserveMapper.findAllReserve(rb);
			
			map.put("pi", pi);
			map.put("reserveList", reserveList);
			
		}
		
		return map;
	}

	@Override
	public ReserveDTO findByReserveNo(Long reserveNo) {
		
		log.info("컨트롤러에서 서비스로 예약 번호 넘어오나요 : {}", reserveNo);
		
		ReserveDTO reserve = reserveMapper.findByReserveNo(reserveNo);
		
		log.info("서비스에서 매퍼 다녀오고 가져온것 : {}", reserve);
		
		return reserve;
		
	}
	
	// 예약 취소
	@Override
	public void deleteByReserveNo(Long reserveNo) {
		
		log.info("컨트롤러에서 서비스로 예약 번호 넘어오나요 : {}", reserveNo);
		reserveMapper.deleteByReserveNo(reserveNo);
		
	}

	// 운영자가 예약 승인하기
	@Override
	public void reserveApprove(Long reserveNo) {
		
		log.info("컨트롤러에서 서비스로 예약번호 넘어오는지 : {}, 예약정보 : {}", reserveNo);
		
		reserveMapper.reserveApprove(reserveNo);
		
		// 만들어둔 예약 조회 기능 써서 차량 번호 가져오기
		ReserveDTO reserve = reserveMapper.findByReserveNo(reserveNo);
		Long carNo = reserve.getCarNo();
		log.info("예약 조회해서 가져온 차량번호 : {}", carNo);
		
		// 차량 번호 보내서 상태 변경해주기
		reserveCarStatus(carNo);
		
	}
	
	@Override
	public void returnCar(Long reserveNo) {
		
		log.info("컨트롤러에서 서비스로 넘어오는 예약번호 : {}", reserveNo);
		
		reserveMapper.returnCar(reserveNo);
		
		log.info("차량 반납 매퍼 다녀옴");
		
	}
	
}
