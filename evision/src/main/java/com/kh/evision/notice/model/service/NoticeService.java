package com.kh.evision.notice.model.service;

import java.util.List;
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
}