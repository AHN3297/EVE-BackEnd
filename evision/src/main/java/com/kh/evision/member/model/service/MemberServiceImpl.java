package com.kh.evision.member.model.service;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.evision.auth.model.vo.CustomUserDetails;
import com.kh.evision.exception.custom.member.CustomAuthenticationException;
import com.kh.evision.exception.custom.member.IdDuplicateException;
import com.kh.evision.exception.custom.member.NicknameDuplicateException;
import com.kh.evision.member.model.dao.MemberMapper;
import com.kh.evision.member.model.dto.ChangePasswordDTO;
import com.kh.evision.member.model.dto.MemberDTO;
import com.kh.evision.member.model.vo.MemberVO;
import com.kh.evision.token.model.dao.TokenMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;
    private final TokenMapper tokenMapper; 
    
    
    @Override
    public int signUp(MemberDTO member) {
        // 아이디 중복 확인
        int count = memberMapper.countByMemberId(member.getMemberId());
        if(1 == count) {
            throw new IdDuplicateException("이미 존재하는 아이디입니다.");
        }
        
        // 닉네임 중복 확인
        int countNick = memberMapper.countByNickname(member.getNickname());
        if(1 == countNick) {
            throw new NicknameDuplicateException("이미 존재하는 닉네임입니다.");
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
        	    .roleStatus(member.getRoleStatus())
        	    .build();
        int result = memberMapper.signUp(memberVO);
        log.info("사용자 등록 성공 : {}", memberVO);
		return result;

    }


	@Override
	public void changePassword(ChangePasswordDTO password) {
		CustomUserDetails user = validatePassword(password.getCurrentPassword());
		String newPassword = passwordEncoder.encode(password.getNewPassword());
		Map<String, Object> changeRequest = Map.of("memberNo", user.getUsername(),
												   "newPassword", newPassword);
	
		memberMapper.changePassword(changeRequest);
	
	}
    
	
	private CustomUserDetails validatePassword(String password) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails user = (CustomUserDetails)auth.getPrincipal();
		
		
		if(!passwordEncoder.matches(password, user.getPassword())) {
			throw new CustomAuthenticationException("비밀번호가 일치하지 않습니다.");
		}
		return user;
	} 
    
    
 }
    
