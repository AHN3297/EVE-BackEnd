package com.kh.evision.configuration;

import java.util.Arrays;

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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
					   
					   requests.requestMatchers(HttpMethod.POST, "/member/login").permitAll();
					   requests.requestMatchers(HttpMethod.PUT, "/boards/**", "/comments/**", "/notice/**", "/cars/**", "/reserve/**", "/station/**", "/reports/**", "/uploads/**", "/member/**").authenticated();
					   requests.requestMatchers(HttpMethod.DELETE, "/boards/**", "/comments/**", "/notice/**", "/cars/**", "/reserve/**", "/station/**", "/reports/**", "/uploads/**", "/member/**").authenticated();
					   requests.requestMatchers(HttpMethod.PATCH, "/boards/**", "/comments/**", "/notice/**", "/cars/**", "/reserve/**", "/station/**", "/reports/**", "/uploads/**", "/member/**").authenticated();
					   requests.requestMatchers(HttpMethod.POST, "/boards/**", "/comments/**", "/notice/**", "/cars/**", "/reserve/**", "/station/**", "/reports/**", "/uploads/**", "/member").authenticated();
					   // requests.requestMatchers("/admin/**").hasRole("ADMIN"); // 권한검증방법
					   requests.requestMatchers(HttpMethod.GET, "/boards", "/comments", "/notice", "/cars", "/station").permitAll();
//					   requests.requestMatchers("/uploads/**").permitAll();
					   
				   })
				   .sessionManagement(manager ->
						   				manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				   .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
				   .build();
	}
	/*
	// CORS 설정 Bean
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		
		// 허용할 출처
		configuration.setAllowedOrigins(Arrays.asList(
			"http://localhost:5175",
			"http://localhost:5173", 
			"http://localhost:3000"
		));
		
		// 허용할 HTTP 메소드
		configuration.setAllowedMethods(Arrays.asList(
			"GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
		));
		
		// 허용할 헤더
		configuration.setAllowedHeaders(Arrays.asList("*"));
		
		// 인증 정보 허용
		configuration.setAllowCredentials(true);
		
		// preflight 요청 캐시 시간
		configuration.setMaxAge(3600L);
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		
		return source;
		
	}
	*/

}
