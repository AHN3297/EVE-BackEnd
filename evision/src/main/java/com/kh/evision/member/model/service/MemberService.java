package com.kh.evision.member.model.service;


import java.util.List;

import com.kh.evision.member.model.dto.ChangePasswordDTO;
import com.kh.evision.member.model.dto.ChangeRoleDTO;
import com.kh.evision.member.model.dto.MemberDTO;
import com.kh.evision.member.model.vo.MemberVO;

public interface MemberService {
    int signUp(MemberDTO member);
    
    void changePassword(ChangePasswordDTO password);

    List<MemberVO> memberManage();
    
	boolean changeRole(ChangeRoleDTO change, String actingRole);
	
	
}
