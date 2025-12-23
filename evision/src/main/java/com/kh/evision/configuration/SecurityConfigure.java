package com.kh.evision.configuration;
import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
					   // 인증 없이 허용 (로그인, 회원가입)
					   requests.requestMatchers(HttpMethod.POST, "/member/join", "/member/login", "/auth/login").permitAll();
					   requests.requestMatchers(HttpMethod.POST, "/member/**").permitAll();
					  
					   // 권한별 제한
					   requests.requestMatchers(HttpMethod.GET, "/member/operator/**").hasRole("OPERATOR");
					   requests.requestMatchers(HttpMethod.POST, "/member/admin/**").hasRole("ADMIN");
					  
					   // 인증 필요 - 내 정보 조회
					   requests.requestMatchers(HttpMethod.GET, "/member/info").authenticated();
					  
					   // 인증 필요 - 내 신고 목록 조회
					   requests.requestMatchers(HttpMethod.GET, "/reports/my").authenticated();
					  
					   // 인증 필요 - 충전소 등록/삭제
					   requests.requestMatchers(HttpMethod.POST, "/station/**").authenticated();
					   requests.requestMatchers(HttpMethod.DELETE, "/station/**").authenticated();
					  
					   // 인증 필요 - 리뷰 등록/수정/삭제
					   requests.requestMatchers(HttpMethod.POST, "/station/reviews").authenticated();
					   requests.requestMatchers(HttpMethod.PUT, "/station/reviews").authenticated();
					   requests.requestMatchers(HttpMethod.DELETE, "/station/reviews/**").authenticated();
					  
					   // 인증 필요 - 신고 등록/삭제
					   requests.requestMatchers(HttpMethod.POST, "/reports").authenticated();
					   requests.requestMatchers(HttpMethod.DELETE, "/reports").authenticated();
					  
					   // 인증 필요 - 기타 쓰기 작업
					   requests.requestMatchers(HttpMethod.POST, "/boards/**", "/comments/**", "/notice/**", "/reserve/**", "/uploads/**").authenticated();
					   requests.requestMatchers(HttpMethod.PUT, "/boards/**", "/comments/**", "/notice/**", "/cars/**", "/reserve/**", "/uploads/**", "/member/**").authenticated();
					   requests.requestMatchers(HttpMethod.DELETE, "/boards/**", "/comments/**", "/notice/**", "/cars/**", "/reserve/**", "/uploads/**", "/member/**").authenticated();
					   requests.requestMatchers(HttpMethod.PATCH, "/boards/**", "/comments/**", "/notice/**", "/cars/**", "/reserve/**", "/uploads/**", "/member/**").authenticated();
					  
					   // 테스트용 임시허용 - cars
					   requests.requestMatchers(HttpMethod.POST, "/cars/**").permitAll();
					   requests.requestMatchers(HttpMethod.GET, "/cars/**").permitAll();
					   requests.requestMatchers(HttpMethod.GET, "/reserve/operator/**")
				        .hasAnyRole("OPERATOR", "ADMIN");
					   requests.requestMatchers(HttpMethod.PATCH, "/reserve/operator/**")
				        .hasAnyRole("OPERATOR", "ADMIN");
					   requests.requestMatchers(HttpMethod.GET, "/reserve/**").authenticated();

					  
					   // 인증 없이 조회 허용
					   requests.requestMatchers(HttpMethod.GET, "/boards", "/boards/**", "/comments", "/comments/**", "/notice", "/notice/**", "/uploads/**").permitAll();
					   requests.requestMatchers(HttpMethod.GET, "/station", "/station/**").permitAll();
					   requests.requestMatchers(HttpMethod.GET, "/reports", "/reports/**").permitAll();
					   requests.requestMatchers(HttpMethod.GET, "/api/**").permitAll();
					   requests.requestMatchers(HttpMethod.GET, "/member/**").permitAll();
					 
					  
					   // 신고 상태 변경 (관리자용)
					   requests.requestMatchers(HttpMethod.PUT, "/reports").authenticated();
					   
					   requests.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
				   })
				   .sessionManagement(manager ->
						   				manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				   .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
				   .build();
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:5174","http://18.176.62.81"/*, "http://57.183.24.239"*/));
		configuration.addAllowedOriginPattern("*");
		configuration.setAllowedMethods(Arrays.asList(
		    "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
		));
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();	
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
	 
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception{
		return authConfig.getAuthenticationManager();
	}
	
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
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







