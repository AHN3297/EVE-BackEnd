package com.kh.evision.member.model.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.evision.exception.custom.member.IdDuplicateException;
import com.kh.evision.exception.custom.member.LoginFailException;
import com.kh.evision.member.model.dao.MemberMapper;
import com.kh.evision.member.model.dto.MemberDTO;
import com.kh.evision.member.model.vo.MemberVO;
import com.kh.evision.token.member.dao.TokenMapper;
import com.kh.evision.token.util.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;  
    private final TokenMapper tokenMapper; 
    
    
    @Override
    public int signUp(MemberDTO member) {
        // 아이디 중복 확인
        int count = memberMapper.countByMemberId(member.getMemberId());
        if(1 == count) {
            throw new IdDuplicateException("이미 존재하는 아이디입니다.");
        }

        // DTO → VO 변환
        MemberVO memberVO = MemberVO.builder()
        	    .memberId(member.getMemberId())
        	    .memberPwd(passwordEncoder.encode(member.getMemberPwd()))
        	    .memberName(member.getMemberName())
        	    .nickname(member.getNickname() != null && !member.getNickname().isEmpty() ? member.getNickname() : "사용자")  // 기본값
        	    .address(member.getAddress() != null && !member.getAddress().isEmpty() ? member.getAddress() : "미입력")      // 기본값
        	    .phone(member.getPhone() != null && !member.getPhone().isEmpty() ? member.getPhone() : "010-0000-0000")      // 기본값
        	    .email(member.getEmail() != null ? member.getEmail() : null)
        	    .status('Y')
        	    .roleStatus("ROLE_USER")
        	    .build();
        int result = memberMapper.signUp(memberVO);
        log.info("사용자 등록 성공 : {}", memberVO);

        return result;
    }
    
    public int login(MemberDTO member) {
        // selectMemberById로 회원 조회
        MemberVO memberVO = memberMapper.selectMemberById(member.getMemberId());

        // 회원이 없거나 비밀번호가 맞지 않으면 예외 발생
        if (memberVO == null || !passwordEncoder.matches(member.getMemberPwd(), memberVO.getMemberPwd())) {
            throw new LoginFailException("아이디 또는 비밀번호가 틀렸습니다.");
        }

        // JWT 토큰 생성 및 DB 저장 (generateToken이 이미 다 처리함)
        String token = jwtUtil.generateToken(memberVO.getMemberNo());
        
        log.info("로그인 성공 - 회원번호: {}, 아이디: {}", memberVO.getMemberNo(), memberVO.getMemberId());

        return 1; // 성공
    
    }
    
 }
    
