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
	    
	    // ⭐ /uploads 경로는 필터 건너뛰기
	    String path = request.getRequestURI();
	    if (path.startsWith("/uploads")) {
	        filterChain.doFilter(request, response);
	        return;
	    }
	    
	    // 기존 JWT 검증 로직...
	    filterChain.doFilter(request, response);
	}
}
