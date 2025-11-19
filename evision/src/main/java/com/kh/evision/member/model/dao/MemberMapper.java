package com.kh.evision.member.model.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.kh.evision.member.model.vo.MemberVO;

@Mapper
public interface MemberMapper {
    
	@Insert("""
		    INSERT INTO TB_MEMBER (
		        MEMBER_NO, MEMBER_NAME, MEMBER_ID, MEMBER_PWD, NICKNAME, ADDRESS, PHONE, EMAIL,
		        ENROLL_DATE, STATUS, ROLE_STATUS
		    ) VALUES (
		        SEQ_MNO.NEXTVAL, #{memberName}, #{memberId}, #{memberPwd}, #{nickname}, #{address}, #{phone}, #{email},
		        SYSDATE, 'Y', #{roleStatus}
		    )
		""")
		int signUp(MemberVO member);
    
    @Select("SELECT COUNT(*) FROM TB_MEMBER WHERE MEMBER_ID = #{memberId}")
    int countByMemberId(String memberId);
    
    @Select("SELECT COUNT(*) FROM TB_MEMBER WHERE NICKNAME=#{nickName}")
    int countByNickname(String nickname);
    
    /**
     * 회원 번호로 회원 조회 (JWT 인증용)
     */
    @Select("""
        SELECT 
            MEMBER_NO AS memberNo,
            MEMBER_NAME AS memberName,
            MEMBER_ID AS memberId,
            MEMBER_PWD AS memberPwd,
            NICKNAME,
            ADDRESS,
            PHONE,
            EMAIL,
            ENROLL_DATE AS enrollDate,
            STATUS,
            ROLE_STATUS AS roleStatus
        FROM TB_MEMBER
        WHERE MEMBER_NO = #{memberNo}
    """)
    MemberVO selectMemberByNo(@Param("memberNo") Long memberNo);
    
    /**
     * 로그인용 - 아이디로 회원 조회
     */
    @Select("""
        SELECT 
            MEMBER_NO AS memberNo,
            MEMBER_NAME AS memberName,
            MEMBER_ID AS memberId,
            MEMBER_PWD AS memberPwd,
            NICKNAME,
            ADDRESS,
            PHONE,
            EMAIL,
            ENROLL_DATE AS enrollDate,
            STATUS,
            ROLE_STATUS AS roleStatus
        FROM TB_MEMBER
        WHERE MEMBER_ID = #{memberId}
    """)
    MemberVO selectMemberById(@Param("memberId") String memberId);
}