package com.kh.evision.station.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.evision.auth.model.vo.CustomUserDetails;
import com.kh.evision.station.model.dto.StationDTO;
import com.kh.evision.station.model.service.StationService;
import com.kh.evision.station.model.vo.ReviewVO;
import com.kh.evision.station.model.vo.StationVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/station")
@RequiredArgsConstructor
public class StationController {

    private final StationService stationService;

    // 충전소 검색
    @GetMapping("/search")
    public ResponseEntity<List<StationDTO>> searchList(
            @RequestParam(name="keyword") String keyword) {
        log.info("충전소 검색 요청 - 키워드: {}", keyword);
        List<StationDTO> stations = stationService.searchList(keyword);
        return ResponseEntity.ok(stations);
    }

    // 충전소 등록
    @PostMapping
    public ResponseEntity<String> save(@RequestBody StationDTO station) {
        log.info("충전소 등록 요청 - 충전소명: {}", station.getStationName());
        stationService.save(station);
        return ResponseEntity.status(HttpStatus.CREATED).body("충전소가 등록되었습니다.");
    }

    // 충전소 목록 보기
    @GetMapping
    public ResponseEntity<List<StationVO>> findAll() {
        log.info("충전소 목록 조회 요청");
        List<StationVO> stations = stationService.findAll();
        return ResponseEntity.ok(stations);
    }

    // 충전소 삭제
    @DeleteMapping
    public ResponseEntity<String> delete(@RequestParam(name="stationNo") Long stationNo) {
        log.info("충전소 삭제 요청 - 충전소 번호: {}", stationNo);
        stationService.delete(stationNo);
        return ResponseEntity.ok("충전소가 삭제되었습니다.");
    }

    // 충전소 상세보기
    @GetMapping("/{stationNo}")
    public ResponseEntity<StationDTO> stationDetail(@PathVariable("stationNo") Long stationNo) {
        log.info("충전소 상세 조회 요청 - 충전소 번호: {}", stationNo);
        StationDTO station = stationService.stationDetail(stationNo);
        return ResponseEntity.ok(station);
    }

    // 충전소 리뷰 등록
    @PostMapping("/reviews")
    public ResponseEntity<String> commentSave(@RequestBody ReviewVO review,
                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        review.setMemberNo(Long.parseLong(userDetails.getUsername()));
        log.info("리뷰 등록 요청 - 충전소 번호: {}, 회원 번호: {}", review.getStationNo(), review.getMemberNo());
        stationService.commentSave(review);
        return ResponseEntity.status(HttpStatus.CREATED).body("리뷰가 등록되었습니다.");
    }

    // 충전소 리뷰 수정
    @PutMapping("/reviews")
    public ResponseEntity<String> commentUpdate(@RequestBody ReviewVO review,
                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        review.setMemberNo(Long.parseLong(userDetails.getUsername()));
        log.info("리뷰 수정 요청 - 리뷰 번호: {}, 회원 번호: {}", review.getReviewNo(), review.getMemberNo());
        stationService.commentUpdate(review);
        return ResponseEntity.ok("리뷰가 수정되었습니다.");
    }

    // 충전소 리뷰 삭제
    @DeleteMapping("/reviews/{reviewNo}")
    public ResponseEntity<String> commentDelete(@PathVariable("reviewNo") Long reviewNo,
                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long memberNo = Long.parseLong(userDetails.getUsername());
        log.info("리뷰 삭제 요청 - 리뷰 번호: {}, 회원 번호: {}", reviewNo, memberNo);
        ReviewVO review = ReviewVO.builder()
                .reviewNo(reviewNo)
                .memberNo(memberNo)
                .build();
        stationService.commentDelete(review);
        return ResponseEntity.ok("리뷰가 삭제되었습니다.");
    }
    
    // 충전소별 리뷰 조회
    @GetMapping("/reviews/{stationNo}")
    public ResponseEntity<List<ReviewVO>> getReviewsByStationNo(@PathVariable("stationNo") Long stationNo) {
        log.info("충전소별 리뷰 조회 요청 - 충전소 번호: {}", stationNo);
        List<ReviewVO> reviews = stationService.findReviewsByStationNo(stationNo);
        return ResponseEntity.ok(reviews);
    }

}

