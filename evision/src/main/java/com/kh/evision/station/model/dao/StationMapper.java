package com.kh.evision.station.model.dao;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kh.evision.station.model.dto.StationDTO;
import com.kh.evision.station.model.vo.ReviewVO;
import com.kh.evision.station.model.vo.StationVO;


@Mapper
public interface StationMapper {
    
    // 충전소 검색
    List<StationDTO> searchList(String keyword);
    
    // 충전소 등록
    int save(StationDTO station);
    
    // 충전소 목록 보기
    List<StationVO> findAll();
    
    // 충전소 삭제
    int delete( Long stationNo);
    
    // 충전소 상세보기
    StationDTO stationDetail( Long stationNo);
    
    // 충전소 리뷰 등록
    int commentSave(ReviewVO review);
    
    // 충전소 리뷰 수정
    int commentUpdate(ReviewVO review);
    
    // 충전소 리뷰 삭제
    int commentDelete(ReviewVO review);
    
    // 충전소별 리뷰 목록 조회
    List<ReviewVO> findReviewsByStationNo(Long stationNo);
}

