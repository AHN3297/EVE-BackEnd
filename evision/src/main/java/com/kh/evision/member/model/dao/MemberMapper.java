package com.kh.evision.member.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.kh.evision.member.model.dto.ChangeRoleDTO;
import com.kh.evision.member.model.dto.LicenseDTO;
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
     * 로그인용 - memberNo로 회원 조회
     */
    @Select("""
        SELECT 
               MEMBER_NO memberNo,
               MEMBER_NAME memberName,
               MEMBER_ID memberId,
               MEMBER_PWD memberPwd,
               NICKNAME nickname,
               ADDRESS address,
               PHONE phone,
               EMAIL email,
               ENROLL_DATE enrollDate,
               STATUS status,
               ROLE_STATUS roleStatus
        FROM TB_MEMBER
        WHERE MEMBER_ID = #{memberId}
    """)
    MemberDTO loadUser(String memberId);
    
    @Select("""
            SELECT 
	               MEMBER_NO memberNo,
	               MEMBER_NAME memberName,
	               MEMBER_ID memberId,
	               MEMBER_PWD memberPwd,
	               NICKNAME nickname,
	               ADDRESS address,
	               PHONE phone,
	               EMAIL email,
	               ENROLL_DATE enrollDate,
	               STATUS status,
	               ROLE_STATUS roleStatus
            FROM TB_MEMBER
            WHERE MEMBER_NO = #{memberNo}
        """)
    MemberDTO loadByMemberNo(String memberNo);

    
    @Select("""
    		SELECT 
                   MEMBER_NO memberNo,
	               MEMBER_NAME memberName,
	               MEMBER_ID memberId,
	               MEMBER_PWD memberPwd,
	               NICKNAME nickname,
	               ADDRESS address,
	               PHONE phone,
	               EMAIL email,
	               ENROLL_DATE enrollDate,
	               STATUS status,
	               ROLE_STATUS roleStatus
              FROM 
                   TB_MEMBER
            ORDER
               BY
                  MEMBER_NO DESC
    		""")
    List<MemberVO> memberManage();
    
    @Update("UPDATE TB_MEMBER SET MEMBER_PWD = #{newPassword} WHERE MEMBER_NO = #{memberNo}")
    int changePassword(Map<String, Object> changeRequest);
    /**
     * mybatis에서 동적 SQl로 null이 아닌 컬럼만 DB에 보내기위해서 사용
     * set은 null이 아닌 필드만 자동으로 updateset문에 포함시켜줌, 콤마 제거는 덤
     * 
     * 
     */
    @Update("""
    	    <script>
    	        UPDATE TB_MEMBER
    	        <set>
    	            <if test="nickname != null">NICKNAME = #{nickname},</if>
    	            <if test="address != null">ADDRESS = #{address},</if>
    	            <if test="phone != null">PHONE = #{phone},</if>
    	            <if test="email != null">EMAIL = #{email},</if>
    	        </set>
    	        WHERE MEMBER_NO = #{memberNo}
    	    </script>
    	""")
    int updateMemberInfo(Map<String, Object> params);
    
    @Update("UPDATE TB_MEMBER SET ROLE_STATUS = #{newRole} WHERE MEMBER_NO = #{memberNo}")
    int changeRole(ChangeRoleDTO change);
    
    @Update("UPDATE TB_MEMBER SET STATUS = 'N' WHERE MEMBER_NO = #{memberNo}")
    int softDelete(String memberNo);
    
    
    @Insert("""
    		INSERT 
    		  INTO 
    		       TB_LICENSE 
    		       (
    		       LICENSE_ID,
    		       MEMBER_NO,
    		       LICENSE_NO,
    		       RENEW_DATE,
    		       ISSUING_AGENCY,
    		       LICENSE_CLASS
    		       )
    	    VALUES (
    	           SEQ_LID.NEXTVAL,
    	           #{memberNo}, 
    	           #{licenseDTO.licenseNo},
    	           #{licenseDTO.renewDate}, 
    	           #{licenseDTO.issuingAgency},
    	           #{licenseDTO.licenseClass}
    	           )
    	           
    			
    		""")
	void insertLicense(@Param("memberNo")String memberNo, @Param("licenseDTO")LicenseDTO licenseDTO);
    
    @Select("SELECT COUNT(*) FROM TB_LICENSE WHERE MEMBER_NO = #{memberNo}")
	int countLicenseByMemberNo(@Param("memberNo") String memberNo);
	    
}

