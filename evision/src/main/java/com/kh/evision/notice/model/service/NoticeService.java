package com.kh.evision.notice.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kh.evision.notice.model.dto.NoticeDTO;
import com.kh.evision.util.PageInfo;

public interface NoticeService {
    
    // 전체 목록
   List<NoticeDTO> getNoticeList(int pageNo);
   PageInfo getPageInfo(int pageNo);
    
    // 검색
   List<NoticeDTO> searchNotices(int pageNo, String keyword);
   PageInfo getSearchPageInfo(int pageNo, String keyword);
    
    // 상세
   NoticeDTO getNoticeDetail(Long noticeNo);
    
   //* 공지사항 등록
   void createNotice(NoticeDTO noticeDTO, MultipartFile thumbnail, List<MultipartFile> files);
   
   /**
    * 공지사항 수정
    */
   
   void updateNotice(NoticeDTO noticeDTO, MultipartFile thumbnail, List<MultipartFile> files);
   
   /**
    * 공지사항 삭제 (논리 삭제)
    */
   void deleteNotice(Long noticeNo);
    

}