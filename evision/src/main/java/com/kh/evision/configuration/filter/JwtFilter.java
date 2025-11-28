package com.kh.evision.configuration.filter;

import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.kh.evision.token.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
	
	private final JwtUtil jwtUtil;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	        throws ServletException, IOException {
	    
	    String path = request.getRequestURI();
	    String method = request.getMethod();
	    
	    // ⭐ 기존 코드 수정: GET만 건너뛰기
	    if (path.startsWith("/uploads") && "GET".equals(method)) {
	        filterChain.doFilter(request, response);
	        return;
	    }
	    
	    // ⭐ 내가 추가한 부분: 공지사항 조회
	    if (path.startsWith("/notice") && "GET".equals(method)) {
	        filterChain.doFilter(request, response);
	        return;
	    }
	    
	    // 기존 JWT 검증 로직...
	    filterChain.doFilter(request, response);
	}
}
