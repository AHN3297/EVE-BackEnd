package com.kh.evision.token.model.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.kh.evision.exception.custom.member.CustomAuthenticationException;
import com.kh.evision.token.model.dao.TokenMapper;
import com.kh.evision.token.model.vo.RefreshToken;
import com.kh.evision.token.util.JwtUtil;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {
	private final JwtUtil tokenUtil;
	private final TokenMapper tokenMapper;
	
	public Map<String, String> generateToken(String username) {
		// String accessToken = tokenUtil.getAccessToken(memberNo);
		// String refreshToken = tokenUtil.getRefreshToken(memberNo);
		// log.info("렉세스토큰 : {}, 리프레시토큰 : {}",accessToken, refreshToken);
		Map<String, String> tokens = createTokens(username);
		saveToken(tokens.get("refreshToken"), username);
		return tokens;
		
	}
	private Map<String, String> createTokens(String username) {

		String accessToken = tokenUtil.getAccessToken(username);
		String refreshToken = tokenUtil.getRefreshToken(username);
		
		log.info("엑세스 토큰 : {}, 리프레시 토큰 : {}", accessToken, refreshToken);
		
		Map<String, String> tokens = new HashMap();
		
		tokens.put("accessToken", accessToken);
		tokens.put("refreshToken", refreshToken);
		return tokens;
	}
	private void saveToken(String refreshToken, String username) {
		RefreshToken token = RefreshToken.builder()
				 .token(refreshToken)
				 .memberNo(Long.valueOf(username))
				 .expiration(System.currentTimeMillis() + 3600000L * 72)
				 .build();
		
		tokenMapper.saveToken(token);
	}
	
	public Map<String, String> validateToken(String refreshToken) {
		
		RefreshToken token = tokenMapper.findByToken(refreshToken);
		
		if(token == null || token.getExpiration() < System.currentTimeMillis()) {
			throw new CustomAuthenticationException("이 토큰 나가리");
		}
		Claims claims = tokenUtil.parseJwt(refreshToken);
		String username = claims.getSubject();
		
		return createTokens(username);
	}

}
