package com.kh.evision.configuration.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.kh.evision.token.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
	
	private final JwtUtil jwtUtil;
	private final UserDetailsService userDetailsService;
	
	@Override
	protected void doFilterInternal(
			HttpServletRequest request, 
			HttpServletResponse response, 
			FilterChain filterChain
	) throws ServletException, IOException {
		
		try {
			// 1. 요청 헤더에서 JWT 토큰 추출
			String token = extractTokenFromRequest(request);
			
			// 2. 토큰이 존재하고 유효한지 검증 (JWT 검증 + DB 확인)
			if (token != null && jwtUtil.validateToken(token)) {
				
				// 3. 토큰에서 회원 번호 추출
				Long memberNo = jwtUtil.getMemberNoFromToken(token);
				String username = memberNo.toString();
				
				// 4. 사용자 정보 로드
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				
				// 5. Authentication 객체 생성
				UsernamePasswordAuthenticationToken authentication = 
					new UsernamePasswordAuthenticationToken(
						userDetails, 
						null, 
						userDetails.getAuthorities()
					);
				
				// 6. 요청 정보 설정
				authentication.setDetails(
					new WebAuthenticationDetailsSource().buildDetails(request)
				);
				
				// 7. SecurityContext에 인증 정보 저장
				SecurityContextHolder.getContext().setAuthentication(authentication);
				
				log.debug("JWT 인증 성공 - 회원번호: {}", memberNo);
			}
			
		} catch (Exception e) {
			log.error("JWT 인증 실패: {}", e.getMessage());
			// 인증 실패 시 SecurityContext를 비워서 인증되지 않은 상태로 처리
			SecurityContextHolder.clearContext();
		}
		
		// 8. 다음 필터로 진행
		filterChain.doFilter(request, response);
	}
	
	/**
	 * HTTP 요청 헤더에서 JWT 토큰 추출
	 * Authorization: Bearer {token}
	 */
	private String extractTokenFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7); // "Bearer " 제거
		}
		
		return null;
	}
	
	/**
	 * 특정 경로는 JWT 필터를 거치지 않도록 설정 (선택사항)
	 */
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getRequestURI();
		
		// 로그인, 회원가입 등 인증이 필요없는 경로
		return path.startsWith("/api/auth/") || 
		       path.startsWith("/api/public/") ||
		       path.equals("/api/login") ||
		       path.equals("/api/register") ||
		       path.equals("/member/signup") ||
		       path.equals("/member/login");
	}
}