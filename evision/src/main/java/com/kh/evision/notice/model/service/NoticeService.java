package com.kh.evision.notice.model.service;

import java.util.List;
import com.kh.evision.notice.model.dto.NoticeDTO;
import com.kh.evision.util.PageInfo;

public interface NoticeService {
    
    List<NoticeDTO> getNoticeList(int currentPage, String keyword);
    
    PageInfo getPageInfo(int currentPage, String keyword);
    
    NoticeDTO getNoticeDetail(Long noticeNo);
}