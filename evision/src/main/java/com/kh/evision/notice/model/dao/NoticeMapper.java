package com.kh.evision.notice.model.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.kh.evision.notice.model.vo.NoticeVO;
import com.kh.evision.notice.model.vo.NoticeImageVO;

@Mapper
public interface NoticeMapper {

    @Select("""
        <script>
        SELECT * FROM (
            SELECT ROWNUM AS RN, A.* FROM (
                SELECT NOTICE_NO, NOTICE_TITLE, NOTICE_CONTENT, CREATE_DATE, MEMBER_NO, STATUS
                FROM TB_NOTICE
                WHERE STATUS = 'Y'
                <if test="keyword != null and keyword != ''">
                    AND (
                        NOTICE_TITLE LIKE '%' || #{keyword} || '%'
                        OR NOTICE_CONTENT LIKE '%' || #{keyword} || '%'
                    )
                </if>
                ORDER BY NOTICE_NO DESC
            ) A
        )
        WHERE RN BETWEEN #{startRow} AND #{endRow}
        </script>
    """)
    List<NoticeVO> getNoticeList(
        @Param("startRow") int startRow, 
        @Param("endRow") int endRow,
        @Param("keyword") String keyword
    );

    @Select("""
        <script>
        SELECT COUNT(*) 
        FROM TB_NOTICE 
        WHERE STATUS = 'Y'
        <if test="keyword != null and keyword != ''">
            AND (
                NOTICE_TITLE LIKE '%' || #{keyword} || '%'
                OR NOTICE_CONTENT LIKE '%' || #{keyword} || '%'
            )
        </if>
        </script>
    """)
    int getNoticeCount(@Param("keyword") String keyword);

    @Select("SELECT * FROM TB_NOTICE WHERE NOTICE_NO = #{noticeNo} AND STATUS = 'Y'")
    NoticeVO getNoticeDetail(@Param("noticeNo") Long noticeNo);
    
    // ⭐ 이미지 조회 추가
    @Select("SELECT * FROM TB_NOTICE_IMG WHERE NOTICE_NO = #{noticeNo} AND STATUS = 'Y'")
    List<NoticeImageVO> getNoticeImages(@Param("noticeNo") Long noticeNo);
}
