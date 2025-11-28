package com.kh.evision.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.kh.evision.configuration.filter.JwtFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfigure {
	
	private final JwtFilter jwtFilter;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
	    return httpSecurity
	            .formLogin(AbstractHttpConfigurer::disable)
	            .csrf(AbstractHttpConfigurer::disable)
	            .authorizeHttpRequests(requests -> {
	                
	                // ✅ 공지사항 상세 조회 허용 (매우 중요!)
	                requests.requestMatchers(HttpMethod.GET, "/notice/**").permitAll();
	                
	                // 기타 조회 허용
	                requests.requestMatchers(HttpMethod.GET, "/uploads/**").permitAll();
	                requests.requestMatchers(HttpMethod.GET, "/boards").permitAll();
	                requests.requestMatchers(HttpMethod.GET, "/comments").permitAll();
	                requests.requestMatchers(HttpMethod.GET, "/cars").permitAll();
	                requests.requestMatchers(HttpMethod.GET, "/station").permitAll();
	                
	                // 로그인
	                requests.requestMatchers(HttpMethod.POST, "/member/login").permitAll();
	                requests.requestMatchers(HttpMethod.POST, "/notice/**").authenticated();
	                requests.requestMatchers(HttpMethod.PUT, "/notice/**").authenticated();
	                requests.requestMatchers(HttpMethod.DELETE, "/notice/**").authenticated();
	                
	                // 나머지는 인증 필요
	                requests.anyRequest().authenticated();
	                
	            })
	            .sessionManagement(manager ->
	                    manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
	            .build();
	}
}