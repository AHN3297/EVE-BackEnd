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
     * 공지사항 목록 조회 (페이징)
     */
    @GetMapping("/list")
    public ResponseEntity<?> getNoticeList(
        @RequestParam(value = "currentPage", defaultValue = "1") int currentPage  // ⭐ value 추가!
    ) {
        log.info("공지사항 목록 조회 - 현재 페이지: {}", currentPage);

        List<NoticeDTO> noticeList = noticeService.getNoticeList(currentPage);
        PageInfo pageInfo = noticeService.getPageInfo(currentPage);

        return ResponseEntity.ok()
                .body(Map.of(
                    "noticeList", noticeList,
                    "pageInfo", pageInfo
                ));
    }
    /**
     * 공지사항 상세 조회
     */
    @GetMapping("/{noticeNo}")
    public ResponseEntity<?> getNoticeDetail(@PathVariable("noticeNo") Long noticeNo) {
        log.info("공지사항 상세 조회 - 공지사항 번호: {}", noticeNo);
        
        NoticeDTO notice = noticeService.getNoticeDetail(noticeNo);
        
        if (notice == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(notice);
    }
}