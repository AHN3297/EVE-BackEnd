package com.kh.evision.reserve.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.kh.evision.reserve.model.vo.ReserveVO;

@Mapper
public interface ReserveMapper {
	
	void reserveCar(ReserveVO reserve);

}
