package com.kh.evision.member.model.service;


import com.kh.evision.member.model.dto.ChangePasswordDTO;
import com.kh.evision.member.model.dto.MemberDTO;

public interface MemberService {
    int signUp(MemberDTO member);
    
    void changePassword(ChangePasswordDTO password);
}
