package com.kh.evision.auth.model.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kh.evision.auth.model.vo.CustomUserDetails;
import com.kh.evision.member.model.dao.MemberMapper;
import com.kh.evision.member.model.dto.MemberDTO;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	
	private final MemberMapper mapper;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		MemberDTO user = mapper.loadUser(username);
		
		
		log.info("이거오나요 : {}", user);
		if(user ==null) {
			throw new UsernameNotFoundException("로그인 실패임 ㅋㅋㄹㅃㅃ");
		}
		
		List<GrantedAuthority> authorities = getAuthorities(user);
		
		
		return CustomUserDetails.builder().username(String.valueOf(user.getMemberNo()))
				                          .password(user.getMemberPwd())
				                          .memberName(user.getMemberName())
				                          .authorities(authorities)
				                          .enabled(user.getStatus() != 'N')
				                          .build();
	}
	
	private List<GrantedAuthority> getAuthorities(MemberDTO user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        
        // 관리자 권한 확인 (roleStatus가 "ADMIN"인 경우와 "OPERATOR"인 경우, "USER"인 경우)
        if ("ROLE_ADMIN".equals(user.getRoleStatus())) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else if ("ROLE_OPERATOR".equals(user.getRoleStatus())) {
        	authorities.add(new SimpleGrantedAuthority("ROLE_OPERATOR"));
        } else if("ROLE_USER".equals(user.getRoleStatus())) {
        	authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        
        return authorities;
    }

}
