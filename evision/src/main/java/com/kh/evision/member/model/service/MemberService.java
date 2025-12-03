package com.kh.evision.member.model.service;


import java.util.List;

import com.kh.evision.member.model.dto.ChangePasswordDTO;
import com.kh.evision.member.model.dto.ChangeRoleDTO;
import com.kh.evision.member.model.dto.LicenseDTO;
import com.kh.evision.member.model.dto.MemberDTO;
import com.kh.evision.member.model.dto.UpdateMemberDTO;
import com.kh.evision.member.model.vo.MemberVO;

public interface MemberService {
    int signUp(MemberDTO member);
    
    void changePassword(ChangePasswordDTO password);
    
    MemberDTO getMemberInfo(String memberNo);

    List<MemberVO> memberManage();
    
	boolean changeRole(ChangeRoleDTO change, String actingRole);

	void updateMemberInfo(String memberNo, UpdateMemberDTO updateDto);

	void deleteMyAccount(String memberNo, String password);

	void deleteMemberByAdmin(String memberNo, String actingRole, String actingMemberNo);

	void verifyLicense(String memberNo, LicenseDTO licenseDTO);
		
	
	
	
}
