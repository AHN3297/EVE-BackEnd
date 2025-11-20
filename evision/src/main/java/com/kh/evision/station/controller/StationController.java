package com.kh.evision.station.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.evision.station.model.service.StationService;
import com.kh.evision.station.model.vo.ReviewVO;
import com.kh.evision.station.model.vo.StationVO;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/station")
@RequiredArgsConstructor
public class StationController {

    private final StationService stationService;

    // 충전소 검색
    @GetMapping("/search")
    public ResponseEntity<List<StationVO>> searchList(
            @RequestParam(required = false) String stationName,
            @RequestParam(required = false) String stationAddress,
            @RequestParam(required = false) String stationType,
            @RequestParam(required = false) Long stationLng,
            @RequestParam(required = false) Long stationLat) {
        List<StationVO> stations = stationService.searchList(stationName, stationAddress, stationType,  stationLng,  stationLat);
        return ResponseEntity.ok(stations);
    }

    // 충전소 등록
    @PostMapping
    public ResponseEntity<String> save(@RequestBody StationVO station) {
    	System.out.println("나는 컨트롤러");
        int result = stationService.save(station);
        if (result > 0) {
            return ResponseEntity.status(HttpStatus.CREATED).body("충전소가 등록되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("충전소 등록에 실패했습니다.");
        }
    }

    // 충전소 목록 보기
    @GetMapping
    public ResponseEntity<List<StationVO>> findAll() {
        List<StationVO> stations = stationService.findAll();
        return ResponseEntity.ok(stations);
    }

    // 충전소 삭제
    @DeleteMapping("/{stationNo}")
    public ResponseEntity<String> delete(@PathVariable Long stationNo) {
        int result = stationService.delete(stationNo);
        if (result > 0) {
            return ResponseEntity.ok("충전소가 삭제되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("충전소를 찾을 수 없습니다.");
        }
    }

    // 충전소 상세보기
    @GetMapping("/{stationNo}")
    public ResponseEntity<StationVO> stationDetail(@PathVariable Long stationNo) {
    	StationVO station = stationService.stationDetail(stationNo);
        if (station != null) {
            return ResponseEntity.ok(station);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // 충전소 리뷰 등록
    @PostMapping("/review")
    public ResponseEntity<String> commentSave(@RequestBody ReviewVO review) {
        int result = stationService.commentSave(review);
        if (result > 0) {
            return ResponseEntity.status(HttpStatus.CREATED).body("리뷰가 등록되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("리뷰 등록에 실패했습니다.");
        }
    }

    // 충전소 리뷰 수정
    @PutMapping("/review")
    public ResponseEntity<String> commentUpdate(@RequestBody ReviewVO review) {
        int result = stationService.commentUpdate(review);
        if (result > 0) {
            return ResponseEntity.ok("리뷰가 수정되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("리뷰를 찾을 수 없습니다.");
        }
    }

    // 충전소 리뷰 삭제
    @DeleteMapping("/review/{reviewNo}")
    public ResponseEntity<String> commentDelete(@PathVariable Long reviewNo) {
        int result = stationService.commentDelete(reviewNo);
        if (result > 0) {
            return ResponseEntity.ok("리뷰가 삭제되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("리뷰를 찾을 수 없습니다.");
        }
    }
}

