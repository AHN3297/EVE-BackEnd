package com.kh.evision.auth.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.evision.ResponseDTO.ResponseData;
import com.kh.evision.auth.model.service.AuthService;
import com.kh.evision.member.model.dto.MemberDTO;
import com.kh.evision.token.model.service.TokenService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthService authService;
	private final TokenService tokenService;
	
	@PostMapping("/login")
	public ResponseEntity<ResponseData<Object>> login(@Valid @RequestBody MemberDTO member){
		
		Map<String, String> loginResponse = authService.login(member);
		
		return ResponseData.ok(loginResponse, "로그인 성공");
	}
	
	@PostMapping("/refresh")
	public ResponseEntity<ResponseData<Object>> refresh(@RequestBody Map<String, String> token) {
		String refreshToken = token.get("refreshToken");
		Map<String, String> tokens= tokenService.validateToken(refreshToken);
		return ResponseData.ok(tokens);
	}

}
