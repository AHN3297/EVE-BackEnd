package com.kh.evision.notice.model.service;

import java.util.List;

import com.kh.evision.notice.model.dto.NoticeDTO;
import com.kh.evision.util.PageInfo;

public interface NoticeService {

	// 공지 목록 조회 관련(페이징 처리)
	List<NoticeDTO> getNoticeList(int currentPage);
	
	// 페이징 정보 조회 관련
	PageInfo getPageInfo(int currentPage);
	
	// 공지 상세 조회 관련
	NoticeDTO getNoticeDetail(Long noticeNo);
	
}
