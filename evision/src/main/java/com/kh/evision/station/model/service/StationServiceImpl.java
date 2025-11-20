package com.kh.evision.station.model.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.evision.station.model.dao.StationMapper;
import com.kh.evision.station.model.vo.ReviewVO;
import com.kh.evision.station.model.vo.StationVO;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class StationServiceImpl implements StationService {

    private final StationMapper stationMapper;

    @Override
    public List<StationVO> searchList(String stationName, String stationAddress, String stationType, Long stationLng, Long stationLat) {
    	System.out.println("나 넘어옴?");
        return stationMapper.searchList(stationName, stationAddress, stationType);
    }

    @Override
    public int save(StationVO station) {
        return stationMapper.save(station);
    }

    @Override
    public List<StationVO> findAll() {
        return stationMapper.findAll();
    }

    @Override
    public int delete(Long stationNo) {
        return stationMapper.delete(stationNo);
    }

    @Override
    public StationVO stationDetail(Long stationNo) {
        return stationMapper.stationDetail(stationNo);
    }

    @Override
    public int commentSave(ReviewVO review) {
        return stationMapper.commentSave(review);
    }

    @Override
    public int commentUpdate(ReviewVO review) {
        return stationMapper.commentUpdate(review);
    }

    @Override
    public int commentDelete(Long reviewNo) {
        return stationMapper.commentDelete(reviewNo);
    }
}

