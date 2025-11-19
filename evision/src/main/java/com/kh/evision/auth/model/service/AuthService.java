package com.kh.evision.auth.model.service;

import java.util.Map;

import com.kh.evision.member.model.dto.MemberDTO;

public interface AuthService {
	Map<String, String> login(MemberDTO member);

}
