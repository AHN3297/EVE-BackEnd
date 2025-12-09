package com.kh.evision.notice.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kh.evision.notice.model.vo.NoticeFileVO;
import com.kh.evision.notice.model.vo.NoticeImageVO;
import com.kh.evision.notice.model.vo.NoticeVO;

@Mapper
public interface NoticeMapper {
    
    /**
     * 공지사항 목록 조회 (페이징, 검색)
     */
    List<NoticeVO> getNoticeList(
        @Param("startRow") int startRow, 
        @Param("endRow") int endRow,
        @Param("keyword") String keyword
    );
    
    /**
     * 공지사항 개수 조회 (검색 조건 포함)
     */
    int getNoticeCount(@Param("keyword") String keyword);
    
    /**
     * 공지사항 상세 조회
     */
    NoticeVO getNoticeDetail(@Param("noticeNo") Long noticeNo);
    
    /**
     * 공지사항 이미지 목록 조회
     */
    List<NoticeImageVO> getNoticeImages(@Param("noticeNo") Long noticeNo);
    
    /**
     * 공지사항 등록
     */
    int insertNotice(NoticeVO noticeVO);
    
    /**
     * 공지사항 수정
     */
    int updateNotice(NoticeVO noticeVO);
    
    /**
     * 공지사항 삭제 (soft delete)
     */
    int deleteNotice(@Param("noticeNo") Long noticeNo);
    
    /**
     * 공지사항 이미지 등록
     */
    int insertNoticeImage(NoticeImageVO imageVO);
    
    /**
     * 공지사항 이미지 삭제 (soft delete)
     */
    int deleteNoticeImages(@Param("noticeNo") Long noticeNo);
    
    List<NoticeFileVO> getNoticeFiles(Long noticeNo);
    int insertNoticeFile(NoticeFileVO noticeFile);
 // 특정 공지사항의 대표 이미지 삭제
    void deleteThumbnailByNoticeNo(Long noticeNo);

    // 특정 공지사항의 첨부 파일 삭제
    void deleteFilesByNoticeNo(Long noticeNo);
    
}