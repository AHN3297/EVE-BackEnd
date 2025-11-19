package com.kh.evision.auth.model.vo;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Builder;
import lombok.Value;


@Value // AllargsConstructor, Getter, ToString 
@Builder
public class CustomUserDetails implements UserDetails {
		private String username; // MEMBER_ID컬럼값 담는 용도
		private String password;
		private String memberName;
		private Collection<? extends GrantedAuthority> authorities;

}