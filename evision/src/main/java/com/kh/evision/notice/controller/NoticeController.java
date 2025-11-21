package com.kh.evision.notice.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.evision.notice.model.dto.NoticeDTO;
import com.kh.evision.notice.model.service.NoticeService;
import com.kh.evision.util.PageInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    /**
     * 공지사항 전체 목록 조회
     */
    @GetMapping("/list")
    public ResponseEntity<?> getNoticeList(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo) {
        
        log.info("공지사항 목록 조회 - 현재 페이지: {}", pageNo);

        List<NoticeDTO> noticeList = noticeService.getNoticeList(pageNo);
        PageInfo pageInfo = noticeService.getPageInfo(pageNo);

        return createResponse(noticeList, pageInfo);
    }

    /**
     * 공지사항 검색
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchNotices(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "keyword") String keyword) {
        
        log.info("공지사항 검색 - 현재 페이지: {}, 검색어: {}", pageNo, keyword);

        List<NoticeDTO> noticeList = noticeService.searchNotices(pageNo, keyword);
        PageInfo pageInfo = noticeService.getSearchPageInfo(pageNo, keyword);

        return createResponse(noticeList, pageInfo);
    }

    /**
     * 공지사항 상세 조회
     */
    @GetMapping("/{noticeNo}")
    public ResponseEntity<?> getNoticeDetail(@PathVariable("noticeNo") Long noticeNo) {
        log.info("공지사항 상세 조회 - 공지사항 번호: {}", noticeNo);

        NoticeDTO notice = noticeService.getNoticeDetail(noticeNo);

        return notice != null 
                ? ResponseEntity.ok(notice) 
                : ResponseEntity.notFound().build();
    }

    /**
     * 공통 응답 생성
     */
    private ResponseEntity<?> createResponse(List<NoticeDTO> noticeList, PageInfo pageInfo) {
        return ResponseEntity.ok()
                .body(Map.of(
                    "noticeList", noticeList,
                    "pageInfo", pageInfo
                ));
    }
}