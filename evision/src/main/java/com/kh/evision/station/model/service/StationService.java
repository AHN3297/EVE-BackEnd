package com.kh.evision.station.model.service;



import java.util.List;

import com.kh.evision.station.model.vo.ReviewVO;
import com.kh.evision.station.model.vo.StationVO;

public interface StationService {
    
    // 충전소 검색
    List<StationVO> searchList(String stationName, String stationAddress, String stationType, Long stationLng, Long stationLat);
    
    // 충전소 등록
    int save(StationVO station);
    
    // 충전소 목록 보기
    List<StationVO> findAll();
    
    // 충전소 삭제
    int delete(Long stationNo);
    
    // 충전소 상세보기
    StationVO stationDetail(Long stationNo);
    
    // 충전소 리뷰 등록
    int commentSave(ReviewVO review);
    
    // 충전소 리뷰 수정
    int commentUpdate(ReviewVO review);
    
    // 충전소 리뷰 삭제
    int commentDelete(Long reviewNo);
}

