package com.kh.evision.station.model.service;


import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.evision.exception.InvalidParameterException;
import com.kh.evision.exception.custom.station.ReviewNotFoundException;
import com.kh.evision.exception.custom.station.StationNotFoundException;
import com.kh.evision.exception.custom.station.UnauthorizedReviewAccessException;
import com.kh.evision.station.model.dao.StationMapper;
import com.kh.evision.station.model.dto.StationDTO;
import com.kh.evision.station.model.vo.ReviewVO;
import com.kh.evision.station.model.vo.StationVO;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@Transactional
public class StationServiceImpl implements StationService {

    private final StationMapper stationMapper;

    @Override
    @Transactional(readOnly = true)
    public List<StationDTO> searchList(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new InvalidParameterException("검색어를 입력해주세요.");
        }
        return stationMapper.searchList(keyword.trim());
    }

    @Override
    public int save(StationDTO station) {
        validateStationDTO(station);
        return stationMapper.save(station);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StationVO> findAll() {
        return stationMapper.findAll();
    }

    @Override
    public int delete(Long stationNo) {
        if (stationNo == null) {
            throw new InvalidParameterException("충전소 번호가 필요합니다.");
        }
        validateStationExists(stationNo);
        return stationMapper.delete(stationNo);
    }

    @Override
    @Transactional(readOnly = true)
    public StationDTO stationDetail(Long stationNo) {
        if (stationNo == null) {
            throw new InvalidParameterException("충전소 번호가 필요합니다.");
        }
        StationDTO station = stationMapper.stationDetail(stationNo);
        if (station == null) {
            throw new StationNotFoundException(stationNo);
        }
        return station;
    }

    @Override
    public int commentSave(ReviewVO review) {
        validateReviewVO(review);
        validateStationExists(review.getStationNo());
        return stationMapper.commentSave(review);
    }

    @Override
    public int commentUpdate(ReviewVO review) {
        validateReviewVO(review);
        if (review.getReviewNo() == null) {
            throw new InvalidParameterException("리뷰 번호가 필요합니다.");
        }
        
        // 리뷰 존재 여부 및 권한 확인
        ReviewVO existingReview = stationMapper.findReviewByReviewNo(review.getReviewNo());
        if (existingReview == null) {
            throw new ReviewNotFoundException(review.getReviewNo());
        }
        if (!existingReview.getMemberNo().equals(review.getMemberNo())) {
            throw new UnauthorizedReviewAccessException();
        }
        
        int result = stationMapper.commentUpdate(review);
        if (result == 0) {
            throw new ReviewNotFoundException("리뷰 수정에 실패했습니다.");
        }
        return result;
    }

    @Override
    public int commentDelete(ReviewVO review) {
        if (review.getReviewNo() == null) {
            throw new InvalidParameterException("리뷰 번호가 필요합니다.");
        }
        
        // 리뷰 존재 여부 및 권한 확인
        ReviewVO existingReview = stationMapper.findReviewByReviewNo(review.getReviewNo());
        if (existingReview == null) {
            throw new ReviewNotFoundException(review.getReviewNo());
        }
        if (!existingReview.getMemberNo().equals(review.getMemberNo())) {
            throw new UnauthorizedReviewAccessException();
        }
        
        int result = stationMapper.commentDelete(review);
        if (result == 0) {
            throw new ReviewNotFoundException("리뷰 삭제에 실패했습니다.");
        }
        return result;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ReviewVO> findReviewsByStationNo(Long stationNo) {
        if (stationNo == null) {
            throw new InvalidParameterException("충전소 번호가 필요합니다.");
        }
        validateStationExists(stationNo);
        return stationMapper.findReviewsByStationNo(stationNo);
    }
    
    // ==================== 유효성 검증 헬퍼 메서드 ====================
    
    private void validateStationExists(Long stationNo) {
        if (stationMapper.existsByStationNo(stationNo) == 0) {
            throw new StationNotFoundException(stationNo);
        }
    }
    
    private void validateStationDTO(StationDTO station) {
        if (station == null) {
            throw new InvalidParameterException("충전소 정보가 필요합니다.");
        }
        if (station.getStationName() == null || station.getStationName().trim().isEmpty()) {
            throw new InvalidParameterException("충전소 이름은 필수 항목입니다.");
        }
        if (station.getStationAddress() == null || station.getStationAddress().trim().isEmpty()) {
            throw new InvalidParameterException("충전소 주소는 필수 항목입니다.");
        }
    }
    
    private void validateReviewVO(ReviewVO review) {
        if (review == null) {
            throw new InvalidParameterException("리뷰 정보가 필요합니다.");
        }
        if (review.getStationNo() == null) {
            throw new InvalidParameterException("충전소 번호가 필요합니다.");
        }
        if (review.getReviewContent() == null || review.getReviewContent().trim().isEmpty()) {
            throw new InvalidParameterException("리뷰 내용은 필수 항목입니다.");
        }
    }

}

