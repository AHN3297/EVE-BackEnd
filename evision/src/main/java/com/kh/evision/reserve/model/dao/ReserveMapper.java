package com.kh.evision.reserve.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.kh.evision.reserve.model.dto.ReserveDTO;
import com.kh.evision.reserve.model.vo.ReserveVO;

@Mapper
public interface ReserveMapper {
	
	// 차량 예약 등록
	void reserveCar(ReserveVO reserve);
	
	// 예약하면서 차량 예약 상태 변경 -> 운영자가 예약 승인하면 변경해야함!
	void reserveCarStatus(Long carNo);
	
	// 예약 전체 개수 조회(사용자용)
	int selectUserTotalCount(Long memberNo);
	
	// 예약 전체 개수 조회(운영자용)
	int selectTotalCount();
	
	// 예약 목록 조회(사용자용)
	List<ReserveDTO> findAllUserReserve(RowBounds rb, Long memberNo);

	// 예약 목록 조회(운영자용)
	List<ReserveDTO> findAllReserve(RowBounds rb);
	
	// 예약 상세 조회
	ReserveDTO findByReserveNo(Long reserveNo);
	
	// 예약 취소
	void deleteByReserveNo(Long reserveNo);
	
	// 운영자용 예약 승인
	void reserveApprove(Long reserveNo);

	// 차량 반납
	void returnCar(Long reserveNo);

}
