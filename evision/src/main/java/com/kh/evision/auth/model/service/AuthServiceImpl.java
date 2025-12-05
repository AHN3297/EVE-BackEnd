package com.kh.evision.auth.model.service;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.evision.auth.model.vo.CustomUserDetails;
import com.kh.evision.exception.custom.member.CustomAuthenticationException;
import com.kh.evision.member.model.dto.MemberDTO;
import com.kh.evision.token.model.service.TokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {
	
	private final AuthenticationManager authenticationManager;
	private final TokenService tokenService;

	@Override
	public Map<String, String> login(MemberDTO member) {
	    Authentication auth = null;
	    try {
	        auth = authenticationManager.authenticate(
	            new UsernamePasswordAuthenticationToken(member.getMemberId(), member.getMemberPwd())
	        );         
	    } catch(AuthenticationException e) {
	        throw new CustomAuthenticationException("아이디 또는 비밀번호를 확인하시고 관리자에게 문의해주세요");
	    }

	    CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();


	    log.info("로그인이 성공");
	    log.info("인증에 성공한 사용자의 정보 : {}", user);

	    Map<String, String> loginResponse = tokenService.generateToken(user.getUsername());
	    loginResponse.put("memberNo", user.getUsername()); 
	    loginResponse.put("memberName", user.getMemberName());

	    String role = user.getAuthorities().stream()
	                .map(autho -> autho.getAuthority())
	                .collect(Collectors.joining(",")); 
	    loginResponse.put("role", role);

	    return loginResponse;
	}

}
