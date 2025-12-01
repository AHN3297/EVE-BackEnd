package com.kh.evision.reserve.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.kh.evision.reserve.model.dto.ReserveDTO;
import com.kh.evision.reserve.model.vo.ReserveVO;

@Mapper
public interface ReserveMapper {
	
	// 차량 예약 등록
	void reserveCar(ReserveVO reserve);
	
	// 예약 전체 개수 조회(사용자용)
	int selectUserTotalCount();
	
	// 예약 전체 개수 조회(운영자용)
	int selectTotalCount();
	
	// 예약 목록 조회(사용자용)
	List<ReserveDTO> findAllUserReserve(RowBounds rb, Long memberNo);

	// 예약 목록 조회(운영자용)
	List<ReserveDTO> findAllReserve(RowBounds rb);

}
