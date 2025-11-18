package com.kh.evision.member.model.service;

import com.kh.evision.member.model.dto.LoginDTO;
import com.kh.evision.member.model.dto.LoginResponseDTO;
import com.kh.evision.member.model.dto.MemberDTO;

public interface MemberService {
    int signUp(MemberDTO member);
    
    LoginResponseDTO login(LoginDTO loginDTO);
}
