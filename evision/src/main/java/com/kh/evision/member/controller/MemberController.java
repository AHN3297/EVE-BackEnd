package com.kh.evision.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.evision.member.model.dto.LoginDTO;
import com.kh.evision.member.model.dto.LoginResponseDTO;
import com.kh.evision.member.model.dto.MemberDTO;
import com.kh.evision.member.model.service.MemberService;

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
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        log.info("로그인 요청 수신: {}", loginDTO.getMemberId());
        LoginResponseDTO response = memberService.login(loginDTO);
        return ResponseEntity.ok(response);
    }
    
    
    // 로그 테스트 엔드포인트
    @PostMapping("/log-test")
    public ResponseEntity<String> logTest() {
        System.out.println("✅ System.out.println 테스트");
        log.info("✅ log.info 테스트");
        return ResponseEntity.ok("로그 테스트 완료");
    }
}