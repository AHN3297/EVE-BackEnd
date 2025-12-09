package com.kh.evision.notice.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kh.evision.auth.model.vo.CustomUserDetails;
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
     * ✅ 공지사항 작성
     * POST /notice/create
     */
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createNotice(
    	@RequestParam(value = "noticeTitle", required = true) String noticeTitle,
    	@RequestParam(value = "noticeContent", required = true) String noticeContent,
    	@RequestParam(value = "memberNo", required = true) Long memberNo,
        @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,
        @RequestPart(value = "files", required = false) List<MultipartFile> files,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        log.info("=== 공지사항 작성 요청 ===");
        log.info("noticeTitle: {}", noticeTitle);
        log.info("noticeContent: {}", noticeContent);
        log.info("memberNo: {}", memberNo);
        
        try {
            // DTO 생성
            NoticeDTO noticeDTO = new NoticeDTO();
            noticeDTO.setNoticeTitle(noticeTitle);
            noticeDTO.setNoticeContent(noticeContent);
            noticeDTO.setMemberNo(memberNo);
            noticeDTO.setStatus('Y');
            
            // 권한 체크
            boolean isAdminOrOperator = userDetails.getAuthorities().stream()
                .anyMatch(auth -> 
                    auth.getAuthority().equals("ROLE_ADMIN") || 
                    auth.getAuthority().equals("ROLE_OPERATOR")
                );
            
            if (!isAdminOrOperator) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "공지사항 작성 권한이 없습니다."));
            }
            
            noticeService.createNotice(noticeDTO, thumbnail, files);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "공지사항이 등록되었습니다."));
                    
        } catch (Exception e) {
            log.error("공지사항 등록 실패: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("message", "공지사항 등록에 실패했습니다."));
        }
    }
    
    /**
     * ✅ 공지사항 수정
     * PUT /notice/{noticeNo}
     */
    @PutMapping(value = "/{noticeNo}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateNotice(
        @PathVariable("noticeNo") Long noticeNo,
        @RequestParam(value = "noticeTitle", required = true) String noticeTitle,
        @RequestParam(value = "noticeContent", required = true) String noticeContent,
        @RequestParam(value = "memberNo", required = true) Long memberNo,
        @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,
        @RequestPart(value = "files", required = false) List<MultipartFile> files,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        log.info("=== 공지사항 수정 요청 ===");
        log.info("noticeNo: {}", noticeNo);
        log.info("noticeTitle: {}", noticeTitle);
        log.info("noticeContent: {}", noticeContent);
        log.info("memberNo: {}", memberNo);

        try {
            // 1. 현재 사용자 memberNo
            Long currentMemberNo = Long.parseLong(userDetails.getUsername());
            
            // 2. 관리자/운영자 권한 확인
            boolean isAdminOrOperator = userDetails.getAuthorities().stream()
                .anyMatch(auth -> 
                    auth.getAuthority().equals("ROLE_ADMIN") || 
                    auth.getAuthority().equals("ROLE_OPERATOR")
                );
            
            if (!isAdminOrOperator) {
                log.warn("수정 권한 없음 - memberNo: {}", currentMemberNo);
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "공지사항 수정 권한이 없습니다."));
            }
            
            // 3. 공지사항 조회
            NoticeDTO notice = noticeService.getNoticeDetail(noticeNo);
            
            // 4. 작성자 본인만 수정 가능
            if (!notice.getMemberNo().equals(currentMemberNo)) {
                log.warn("수정 권한 없음 - 작성자가 아님. memberNo: {}, 작성자: {}", 
                    currentMemberNo, notice.getMemberNo());
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "작성자만 수정할 수 있습니다."));
            }
            
            NoticeDTO noticeDTO = new NoticeDTO();
            noticeDTO.setNoticeNo(noticeNo);
            noticeDTO.setNoticeTitle(noticeTitle);
            noticeDTO.setNoticeContent(noticeContent);
            noticeDTO.setMemberNo(memberNo);
            noticeDTO.setStatus('Y');
            
            // 5. 수정 실행
            noticeService.updateNotice(noticeDTO, thumbnail, files);
            log.info("공지사항 수정 완료 - noticeNo: {}", noticeNo);
            
            return ResponseEntity.ok()
                    .body(Map.of("message", "공지사항이 수정되었습니다."));
                    
        } catch (IllegalArgumentException e) {
            log.warn("공지사항 수정 실패: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error("공지사항 수정 실패: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(Map.of("message", "공지사항 수정에 실패했습니다."));
        }
    }
    
    /**
     * ✅ 공지사항 삭제 (논리 삭제)
     * DELETE /notice/{noticeNo}
     */
    @DeleteMapping("/{noticeNo}")
    public ResponseEntity<?> deleteNotice(
        @PathVariable("noticeNo") Long noticeNo,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        log.info("공지사항 삭제 - noticeNo: {}", noticeNo);

        try {
            // 1. 현재 사용자 memberNo (username에 저장되어 있음)
            Long currentMemberNo = Long.parseLong(userDetails.getUsername());
            
            // 2. 관리자 여부 확인
            boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
            
            // 3. 공지사항 조회
            NoticeDTO notice = noticeService.getNoticeDetail(noticeNo);
            
            // 4. 권한 체크: 관리자 OR 작성자 본인
            if (!isAdmin && !notice.getMemberNo().equals(currentMemberNo)) {
                log.warn("삭제 권한 없음 - memberNo: {}, 작성자: {}", currentMemberNo, notice.getMemberNo());
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "삭제 권한이 없습니다."));
            }
            
            // 5. 삭제 실행
            noticeService.deleteNotice(noticeNo);
            log.info("공지사항 삭제 완료 - noticeNo: {}, 삭제자: {}", noticeNo, currentMemberNo);
            
            return ResponseEntity.ok()
                    .body(Map.of("message", "공지사항이 삭제되었습니다."));
                    
        } catch (IllegalArgumentException e) {
            log.warn("공지사항 삭제 실패: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error("공지사항 삭제 실패: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(Map.of("message", "공지사항 삭제에 실패했습니다."));
        }
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