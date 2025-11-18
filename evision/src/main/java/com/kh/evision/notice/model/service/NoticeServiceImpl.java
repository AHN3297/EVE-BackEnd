package com.kh.evision.notice.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.evision.notice.model.dao.NoticeMapper;
import com.kh.evision.notice.model.dto.NoticeDTO;
import com.kh.evision.notice.model.vo.NoticeVO;
import com.kh.evision.util.PageInfo;
import com.kh.evision.util.Pagination;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NoticeServiceImpl implements NoticeService {

    private final NoticeMapper noticeMapper;
    private final Pagination pagination;
    
    private static final int BOARD_LIMIT = 10; // 한 페이지당 게시물 수
    private static final int PAGE_LIMIT = 5;   // 한 화면당 페이지 수

    @Override
    public List<NoticeDTO> getNoticeList(int currentPage) {
        PageInfo pageInfo = getPageInfo(currentPage);
        
        int startRow = (currentPage - 1) * BOARD_LIMIT + 1;
        int endRow = currentPage * BOARD_LIMIT;
        
        List<NoticeVO> noticeList = noticeMapper.getNoticeList(startRow, endRow);
        
        return noticeList.stream()
                .map(vo -> new NoticeDTO(
                    vo.getNoticeNo(),
                    vo.getNoticeTitle(),
                    vo.getNoticeContent(),
                    vo.getCreateDate(),
                    vo.getMemberNo(),
                    vo.getStatus()
                ))
                .toList();
    }

    @Override
    public PageInfo getPageInfo(int currentPage) {
        int listCount = noticeMapper.getNoticeCount();
        return pagination.getPageInfo(listCount, currentPage, BOARD_LIMIT, PAGE_LIMIT);
    }

    @Override
    public NoticeDTO getNoticeDetail(Long noticeNo) {
        NoticeVO noticeVO = noticeMapper.getNoticeDetail(noticeNo);
        
        if (noticeVO == null) {
            return null;
        }
        
        return new NoticeDTO(
            noticeVO.getNoticeNo(),
            noticeVO.getNoticeTitle(),
            noticeVO.getNoticeContent(),
            noticeVO.getCreateDate(),
            noticeVO.getMemberNo(),
            noticeVO.getStatus()
        );
    }
}