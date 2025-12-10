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

		if(user ==null) {
			throw new UsernameNotFoundException("로그인 실패" + username);
		}
		
		List<GrantedAuthority> authorities = getAuthorities(user);
		
		log.info("dd :{}", authorities);
		log.info("사용자 status: {}", user.getStatus());
		log.info("사용자 enabled: {}", user.getStatus() == 'Y');
		return CustomUserDetails.builder().username(String.valueOf(user.getMemberNo()))
				                          .password(user.getMemberPwd())
				                          .memberName(user.getMemberName())
				                          .authorities(authorities)
				                          .enabled(user.getStatus() != 'N')
				                          .build();
	}
	
	private List<GrantedAuthority> getAuthorities(MemberDTO user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        
        // 관리자 권한 확인 (roleStatus가 "ROLE_ADMIN"인 경우와 "ROLE_OPERATOR"인 경우, "ROLE_USER"인 경우)
        if ("ROLE_ADMIN".equals(user.getRoleStatus())) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            authorities.add(new SimpleGrantedAuthority("ROLE_OPERATOR"));
        } else if ("ROLE_OPERATOR".equals(user.getRoleStatus())) {
        	authorities.add(new SimpleGrantedAuthority("ROLE_OPERATOR"));
        } else if("ROLE_USER".equals(user.getRoleStatus())) {
        	authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        
        return authorities;
    }

}
