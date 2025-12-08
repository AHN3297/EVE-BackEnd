package com.kh.evision.notice.model.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kh.evision.notice.model.dao.NoticeMapper;
import com.kh.evision.notice.model.dto.NoticeDTO;
import com.kh.evision.notice.model.vo.NoticeImageVO;
import com.kh.evision.notice.model.vo.NoticeVO;
import com.kh.evision.util.PageInfo;
import com.kh.evision.util.Pagination;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {
    
    private final NoticeMapper noticeMapper;
    private final Pagination pagination;
    
    private static final int BOARD_LIMIT = 10;
    private static final int PAGE_LIMIT = 5;
    
    @Value("${file.upload.path:uploads/}")
    private String uploadPath;
    
    @Override
    @Transactional(readOnly = true)
    public List<NoticeDTO> getNoticeList(int pageNo) {
        return getNotices(pageNo, null);
    }
   
    @Override
    @Transactional(readOnly = true)
    public PageInfo getPageInfo(int pageNo) {
        return getPageInfoByKeyword(pageNo, null);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<NoticeDTO> searchNotices(int pageNo, String keyword) {
        return getNotices(pageNo, keyword);
    }
    
    @Override
    @Transactional(readOnly = true)
    public PageInfo getSearchPageInfo(int pageNo, String keyword) {
        return getPageInfoByKeyword(pageNo, keyword);
    }
    
    private List<NoticeDTO> getNotices(int pageNo, String keyword) {
        int offset = (pageNo - 1) * BOARD_LIMIT;
        int limit = BOARD_LIMIT;
        
        int startRow = offset + 1;
        int endRow = offset + BOARD_LIMIT;
        List<NoticeVO> noticeList = noticeMapper.getNoticeList(startRow, endRow, keyword);
        return noticeList.stream()
                .map(this::convertToDTO)
                .toList();
    }
    
    private PageInfo getPageInfoByKeyword(int pageNo, String keyword) {
        int listCount = noticeMapper.getNoticeCount(keyword);
        return pagination.getPageInfo(listCount, pageNo, BOARD_LIMIT, PAGE_LIMIT);
    }
    
    private NoticeDTO convertToDTO(NoticeVO vo) {
        // 이미지 목록
        List<NoticeImageVO> images = noticeMapper.getNoticeImages(vo.getNoticeNo());
        List<String> imageUrls = images.stream()
            .map(img -> "/uploads/" + img.getChangeName())
            .collect(Collectors.toList());
        
        // 대표 이미지
        String thumbnailUrl = vo.getThumbnailUrl() != null 
            ? "/uploads/" + vo.getThumbnailUrl() 
            : null;
        
        // 첨부 파일 목록
        List<String> fileUrls = noticeMapper.getNoticeFiles(vo.getNoticeNo()).stream()
            .map(file -> "/uploads/" + file.getChangeName())
            .collect(Collectors.toList());
        
        return NoticeDTO.builder()
        	    .noticeNo(vo.getNoticeNo())
        	    .noticeTitle(vo.getNoticeTitle())
        	    .noticeContent(vo.getNoticeContent())
        	    .createDate(vo.getCreateDate())
        	    .memberNo(vo.getMemberNo())
        	    .status(vo.getStatus())
        	    .imageUrls(imageUrls)
        	    .thumbnailUrl(thumbnailUrl)
        	    .fileUrls(fileUrls)
        	    .build();   
        
    }
    
    /**
     * 공지사항 상세 조회 (조회수 증가 포함)
     */
    @Override
    @Transactional
    public NoticeDTO getNoticeDetail(Long noticeNo) {
        log.debug("공지사항 상세 조회 시작 - noticeNo: {}", noticeNo);
        
        // 1. 공지사항 조회
        NoticeVO noticeVO = noticeMapper.getNoticeDetail(noticeNo);
        
        if (noticeVO == null) {
            log.warn("존재하지 않는 공지사항 - noticeNo: {}", noticeNo);
            throw new IllegalArgumentException("해당 공지사항을 찾을 수 없습니다. (noticeNo: " + noticeNo + ")");
        }
        
        // 2. 조회수 증가 (임시로 주석 처리)
         int result = noticeMapper.increaseViewCount(noticeNo);
         if (result > 0) {
             log.debug("조회수 증가 완료 - noticeNo: {}", noticeNo);
         }
        
        // 3. DTO 변환 및 반환
        return convertToDTO(noticeVO);
    }
    /**
     * 공지사항 등록
     */
    @Override
    @Transactional
    public void createNotice(NoticeDTO noticeDTO, MultipartFile thumbnail, List<MultipartFile> files) {  // ✅ 수정
        log.info("공지사항 등록 - 제목: {}", noticeDTO.getNoticeTitle());
        
        // 1. NoticeVO 생성
        NoticeVO noticeVO = new NoticeVO();
        noticeVO.setNoticeTitle(noticeDTO.getNoticeTitle());
        noticeVO.setNoticeContent(noticeDTO.getNoticeContent());
        noticeVO.setMemberNo(noticeDTO.getMemberNo());
        noticeVO.setStatus('Y');
        
        // 2. 공지사항 등록
        int result = noticeMapper.insertNotice(noticeVO);
        
        if (result == 0) {
            throw new RuntimeException("공지사항 등록에 실패했습니다.");
        }
        
        // 3. 대표 이미지 저장
        if (thumbnail != null && !thumbnail.isEmpty()) {
            saveNoticeImage(noticeVO.getNoticeNo(), thumbnail, true);  // ✅ 수정
        }
        
        // 4. 첨부 파일 저장
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                saveNoticeImage(noticeVO.getNoticeNo(), file, false);  // ✅ 수정
            }
        }
        
        log.info("공지사항 등록 완료 - noticeNo: {}", noticeVO.getNoticeNo());
    }
    
    /**
     * 공지사항 수정
     */
    @Override
    @Transactional
    public void updateNotice(NoticeDTO noticeDTO, MultipartFile thumbnail, List<MultipartFile> files) {  // ✅ 수정
        log.info("공지사항 수정 - noticeNo: {}", noticeDTO.getNoticeNo());
        
        // 1. 기존 공지사항 확인
        NoticeVO existingNotice = noticeMapper.getNoticeDetail(noticeDTO.getNoticeNo());
        if (existingNotice == null) {
            throw new IllegalArgumentException("수정할 공지사항을 찾을 수 없습니다.");
        }
        
        // 2. NoticeVO 업데이트
        NoticeVO noticeVO = new NoticeVO();
        noticeVO.setNoticeNo(noticeDTO.getNoticeNo());
        noticeVO.setNoticeTitle(noticeDTO.getNoticeTitle());
        noticeVO.setNoticeContent(noticeDTO.getNoticeContent());
        
        int result = noticeMapper.updateNotice(noticeVO);
        
        if (result == 0) {
            throw new RuntimeException("공지사항 수정에 실패했습니다.");
        }
        
        // 3. 새 대표 이미지가 있으면 저장
        if (thumbnail != null && !thumbnail.isEmpty()) {
            // 기존 대표 이미지 삭제는 선택사항
            saveNoticeImage(noticeDTO.getNoticeNo(), thumbnail, true);
        }
        
        // 4. 새 첨부 파일이 있으면 저장
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                saveNoticeImage(noticeDTO.getNoticeNo(), file, false);
            }
        }
        
        log.info("공지사항 수정 완료 - noticeNo: {}", noticeDTO.getNoticeNo());
    }
    
    /**
     * 공지사항 삭제 (논리 삭제)
     */
    @Override
    @Transactional
    public void deleteNotice(Long noticeNo) {
        log.info("공지사항 삭제 - noticeNo: {}", noticeNo);
        
        // 1. 공지사항 존재 확인
        NoticeVO notice = noticeMapper.getNoticeDetail(noticeNo);
        if (notice == null) {
            throw new IllegalArgumentException("삭제할 공지사항을 찾을 수 없습니다.");
        }
        
        // 2. 논리 삭제 (STATUS = 'N')
        int result = noticeMapper.deleteNotice(noticeNo);
        
        if (result == 0) {
            throw new RuntimeException("공지사항 삭제에 실패했습니다.");
        }
        
        // 3. 이미지도 논리 삭제
        noticeMapper.deleteNoticeImages(noticeNo);
        
        log.info("공지사항 삭제 완료 - noticeNo: {}", noticeNo);
    }
    
    /**
     * 이미지 파일 저장
     */
    private void saveNoticeImage(Long noticeNo, MultipartFile file, boolean isThumbnail) {  // ✅ 수정
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        
        if (file == null || file.isEmpty()) return;
        
        try {
            String originName = file.getOriginalFilename();
            String ext = originName.substring(originName.lastIndexOf("."));
            String changeName = UUID.randomUUID().toString() + ext;
            
            // 파일 저장
            File dest = new File(uploadDir.getAbsolutePath(), changeName);
            file.transferTo(dest);
            
            // DB에 이미지 정보 저장
            NoticeImageVO imageVO = new NoticeImageVO();
            imageVO.setNoticeNo(noticeNo);
            imageVO.setOriginName(originName);
            imageVO.setChangeName(changeName);
            imageVO.setIsThumbnail(isThumbnail ? "Y" : "N");  // ✅
            imageVO.setStatus('Y');
            
            log.info("=== 이미지 저장 ===");  // ✅ 추가
            log.info("noticeNo: {}", noticeNo);
            log.info("originName: {}", originName);
            log.info("isThumbnail 파라미터: {}", isThumbnail);
            log.info("IS_THUMBNAIL 설정값: {}", imageVO.getIsThumbnail());
            
            noticeMapper.insertNoticeImage(imageVO);
            
            log.info("이미지 저장 완료");
        } catch (IOException e) {
            throw new RuntimeException("파일 저장에 실패했습니다.", e);
        }
    }
 }
    
    
