package com.kh.evision.notice.model.service;

import java.util.List;
import java.util.stream.Collectors;

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

    private static final int BOARD_LIMIT = 10;
    private static final int PAGE_LIMIT = 5;

    @Override
    public List<NoticeDTO> getNoticeList(int pageNo) {
        return getNotices(pageNo, null);
    }

    @Override
    public PageInfo getPageInfo(int pageNo) {
        return getPageInfoByKeyword(pageNo, null);
    }

    @Override
    public List<NoticeDTO> searchNotices(int pageNo, String keyword) {
        return getNotices(pageNo, keyword);
    }

    @Override
    public PageInfo getSearchPageInfo(int pageNo, String keyword) {
        return getPageInfoByKeyword(pageNo, keyword);
    }

    private List<NoticeDTO> getNotices(int pageNo, String keyword) {
        // ⭐ offset 계산 (0부터 시작)
        int offset = (pageNo - 1) * BOARD_LIMIT;
        int limit = BOARD_LIMIT;

        // ⭐ RowBounds 사용
        List<NoticeVO> noticeList = noticeMapper.getNoticeList(offset, limit, keyword);

        return noticeList.stream()
                .map(this::convertToDTO)
                .toList();
    }

    private PageInfo getPageInfoByKeyword(int pageNo, String keyword) {
        int listCount = noticeMapper.getNoticeCount(keyword);
        return pagination.getPageInfo(listCount, pageNo, BOARD_LIMIT, PAGE_LIMIT);
    }

    private NoticeDTO convertToDTO(NoticeVO vo) {
        // ⭐ 이미지 조회 및 URL 생성
        List<String> imageUrls = noticeMapper.getNoticeImages(vo.getNoticeNo())
                .stream()
                .map(img -> "/uploads/" + img.getChangeName())
                .collect(Collectors.toList());
        
        return new NoticeDTO(
            vo.getNoticeNo(),
            vo.getNoticeTitle(),
            vo.getNoticeContent(),
            vo.getCreateDate(),
            vo.getMemberNo(),
            vo.getStatus(),
            imageUrls  // ⭐ 이미지 URL 리스트
        );
    }

    @Override
    public NoticeDTO getNoticeDetail(Long noticeNo) {
        NoticeVO noticeVO = noticeMapper.getNoticeDetail(noticeNo);

        if (noticeVO == null) {
            return null;
        }

        return convertToDTO(noticeVO);
    }
}