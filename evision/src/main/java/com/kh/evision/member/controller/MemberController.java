package com.kh.evision.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.evision.member.model.dto.ChangePasswordDTO;
import com.kh.evision.member.model.dto.MemberDTO;
import com.kh.evision.member.model.service.MemberService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/member")
public class MemberController {
    
    private final MemberService memberService;
    
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
    
    // 회원가입 엔드포인트
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Validated @RequestBody MemberDTO member) {
        log.info("회원가입 요청 수신: {}", member);
        int result = memberService.signUp(member);
        if (result > 0) {
            log.info("회원가입 성공: {}", member.getMemberId());
            return ResponseEntity.status(201).body("회원가입 성공");
        } else {
            log.warn("회원가입 실패: {}", member.getMemberId());
            return ResponseEntity.badRequest().body("회원가입 실패");
        }
    }
    
    @PutMapping
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDTO password){
    	memberService.changePassword(password);
    	return ResponseEntity.ok("확인되었습니다.");
    }
    
}