package com.kh.evision.api.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.kh.evision.api.model.dto.ResponseDTO;

@Mapper
public interface ApiDAO {
	void insertStation(ResponseDTO responseDTO); 
}
