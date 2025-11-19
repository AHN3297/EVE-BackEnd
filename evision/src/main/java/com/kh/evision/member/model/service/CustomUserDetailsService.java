package com.kh.evision.member.model.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kh.evision.member.model.dao.MemberMapper;
import com.kh.evision.member.model.vo.MemberVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberMapper memberMapper;

    /**
     * Spring Security에서 사용자 인증 시 호출
     * @param username 회원번호 (문자열)
     * @return UserDetails 객체
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user by username(memberNo): {}", username);
        
        try {
            // 1. 회원번호로 회원 정보 조회
            Long memberNo = Long.parseLong(username);
            MemberVO member = memberMapper.selectMemberByNo(memberNo);
            
            // 2. 회원이 없으면 예외 발생
            if (member == null) {
                log.warn("Member not found with memberNo: {}", memberNo);
                throw new UsernameNotFoundException("회원을 찾을 수 없습니다: " + memberNo);
            }
            
            // 3. 권한 설정
            List<GrantedAuthority> authorities = getAuthorities(member);
            
            // 4. UserDetails 객체 반환
            return User.builder()
                    .username(member.getMemberNo().toString())  // 회원번호를 username으로
                    .password(member.getMemberPwd())             // 비밀번호
                    .authorities(authorities)                    // 권한 목록
                    .accountExpired(false)
                    .accountLocked(false)
                    .credentialsExpired(false)
                    .disabled(member.getStatus() != 'Y')         // STATUS가 'Y'면 활성화
                    .build();
                    
        } catch (NumberFormatException e) {
            log.error("Invalid memberNo format: {}", username);
            throw new UsernameNotFoundException("잘못된 회원번호 형식입니다: " + username);
        }
    }

    /**
     * 회원의 권한 목록 생성
     * @param member 회원 정보
     * @return 권한 목록
     */
    private List<GrantedAuthority> getAuthorities(MemberVO member) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        // 기본 권한 부여
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        
        // 관리자 권한 확인 (roleStatus가 "ADMIN"인 경우)
        if ("ADMIN".equals(member.getRoleStatus())) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        
        return authorities;
    }
}