package com.kh.evision.auth.model.vo;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CustomUserDetails implements UserDetails {
	private String username; // memberNo
	private String password;
	private String memberName;	
	private Collection<? extends GrantedAuthority> authorities;
	private Boolean enabled;

}
