package com.kh.evision.reserve.model.service;

import java.util.Map;

import com.kh.evision.reserve.model.dto.ReserveDTO;

public interface ReserveService {
	
	void reserveCar(ReserveDTO reserve);
	
	Map<String, Object> findAllUserReserve(int pageNo);
	
	Map<String, Object> findAllReserve(int pageNo);
	
	ReserveDTO findByReserveNo(Long reserveNo);

}
