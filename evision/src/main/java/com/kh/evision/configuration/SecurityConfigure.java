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

					   requests.requestMatchers(HttpMethod.POST, "/member/join").permitAll();
					   requests.requestMatchers(HttpMethod.POST, "/member/**").permitAll();
					   requests.requestMatchers(HttpMethod.POST, "/auth/login").permitAll();
					   requests.requestMatchers(HttpMethod.PUT, "/boards/**", "/comments/**", "/notice/**", "/cars/**", "/reserve/**", "/station/**", "/reports/**", "/uploads/**", "/member/**").authenticated();
					   requests.requestMatchers(HttpMethod.DELETE, "/boards/**", "/comments/**", "/notice/**", "/cars/**", "/reserve/**", "/station/**", "/reports/**", "/uploads/**", "/member/**").authenticated();
					   requests.requestMatchers(HttpMethod.PATCH, "/boards/**", "/comments/**", "/notice/**", "/cars/**", "/reserve/**", "/station/**", "/reports/**", "/uploads/**", "/member/**").authenticated();
					   requests.requestMatchers(HttpMethod.POST, "/boards/**", "/comments/**", "/notice/**", /*"/cars/**",*/ "/reserve/**", "/station/**", "/reports/**", "/uploads/**").authenticated();
					   //requests.requestMatchers(HttpMethod.POST, "/operator/**").hasRole("OPERATOR"); // 권한검증방법
					   requests.requestMatchers(HttpMethod.GET, "/member/operator/**").hasRole("OPERATOR");
					   requests.requestMatchers(HttpMethod.POST, "/member/admin/**").hasRole("ADMIN"); // 권한검증방법
					   //requests.requestMatchers(HttpMethod.POST, "/user/**").hasRole("USER"); // 권한검증방법
					   
					   requests.requestMatchers(HttpMethod.GET, "/boards", "/comments", "/notice", "/cars", "/station", "/member/**").permitAll();
					   requests.requestMatchers(HttpMethod.GET, "member/info").authenticated();
					   requests.requestMatchers(HttpMethod.GET, "/boards", "/comments", "/notice", "/cars", "/station").permitAll();
					   requests.requestMatchers(HttpMethod.POST, "/cars/**").permitAll(); // 테스트용 임시허용
					   requests.requestMatchers(HttpMethod.GET, "/cars/**").permitAll(); // 테스트용 임시허용

				   })
				   .sessionManagement(manager ->
						   				manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				   .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
				   .build();  
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
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

	
	
}
