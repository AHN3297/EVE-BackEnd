package com.kh.evision.notice.model.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;  // ⭐ 추가!
import org.apache.ibatis.annotations.Select;
import com.kh.evision.notice.model.vo.NoticeVO;

@Mapper
public interface NoticeMapper {

    /**
     * 공지사항 목록 조회 (페이징)
     */
    @Select("""
        SELECT * FROM (
            SELECT ROWNUM AS RN, A.* FROM (
                SELECT NOTICE_NO, NOTICE_TITLE, NOTICE_CONTENT, CREATE_DATE, MEMBER_NO, STATUS
                FROM TB_NOTICE
                WHERE STATUS = 'Y'
                ORDER BY NOTICE_NO DESC
            ) A
        )
        WHERE RN BETWEEN #{startRow} AND #{endRow}
    """)
    List<NoticeVO> getNoticeList(@Param("startRow") int startRow, @Param("endRow") int endRow);  // ⭐ @Param 추가!

    /**
     * 공지사항 전체 개수
     */
    @Select("SELECT COUNT(*) FROM TB_NOTICE WHERE STATUS = 'Y'")
    int getNoticeCount();

    /**
     * 공지사항 상세 조회
     */
    @Select("SELECT * FROM TB_NOTICE WHERE NOTICE_NO = #{noticeNo} AND STATUS = 'Y'")
    NoticeVO getNoticeDetail(@Param("noticeNo") Long noticeNo);  // ⭐ 이것도 추가하는 게 좋음!
}