package com.kh.evision.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
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
		return httpSecurity.formLogin(AbstractHttpConfigurer::disable)
				   .csrf(AbstractHttpConfigurer::disable)
				   .cors(Customizer.withDefaults())
				   .authorizeHttpRequests(requests -> {
					   
					   // 로그인 - 누구나 접근
		               requests.requestMatchers(HttpMethod.POST, "/member/login").permitAll();
		               // 🔹 공지사항 - 조회/검색은 모두 허용
		               requests.requestMatchers(HttpMethod.GET, "/notice/**").permitAll();
		               // 공지 작성/수정/삭제는 권한자 전용
					   requests.requestMatchers(HttpMethod.POST, "/notice/**").authenticated();
					   requests.requestMatchers(HttpMethod.PUT, "/boards/**", "/comments/**", "/notice/**", "/cars/**", "/reserve/**", "/station/**", "/reports/**", "/uploads/**", "/member/**").authenticated();
					   requests.requestMatchers(HttpMethod.DELETE, "/boards/**", "/comments/**", "/notice/**", "/cars/**", "/reserve/**", "/station/**", "/reports/**", "/uploads/**", "/member/**").authenticated();
					   requests.requestMatchers(HttpMethod.PATCH, "/boards/**", "/comments/**", "/notice/**", "/cars/**", "/reserve/**", "/station/**", "/reports/**", "/uploads/**", "/member/**").authenticated();
					   requests.requestMatchers(HttpMethod.POST, "/boards/**", "/comments/**", "/notice/**", "/cars/**", "/reserve/**", "/station/**", "/reports/**", "/uploads/**", "/member").authenticated();
					   // requests.requestMatchers("/admin/**").hasRole("ADMIN"); // 권한검증방법
					   requests.requestMatchers(HttpMethod.GET, "/boards", "/comments", "/cars", "/station").permitAll();
					   
				   })
				   .sessionManagement(manager ->
						   				manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				   .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
				   .build();
	}

}
