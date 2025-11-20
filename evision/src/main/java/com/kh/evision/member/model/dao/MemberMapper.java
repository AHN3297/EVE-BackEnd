package com.kh.evision.member.model.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.kh.evision.member.model.dto.ChangeRoleDTO;
import com.kh.evision.member.model.dto.MemberDTO;
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
     * 로그인용 - 아이디로 회원 조회
     */
    @Select("""
        SELECT 
            MEMBER_NO memberNo,
            MEMBER_NAME memberName,
            MEMBER_ID memberId,
            MEMBER_PWD memberPwd,
            NICKNAME,
            ADDRESS,
            PHONE,
            EMAIL,
            ENROLL_DATE enrollDate,
            STATUS,
            ROLE_STATUS roleStatus
        FROM TB_MEMBER
        WHERE MEMBER_ID = #{memberId}
    """)
    MemberDTO loadUser(String memberId);
    
    @Update("UPDATE TB_MEMBER SET MEMBER_PWD - #{newPassword} WHERE MEMBER_NO = #{memberNo}")
    String changePassword(Map<String, Object> changeRequest);
    
    @Update("UPDATE MEMBER SET ROLE_STATUS = #{newRole} WHERE MEMBER_NO = #{memberNo}")
    int changeRole(ChangeRoleDTO change);;
}

