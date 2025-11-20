package com.kh.evision.token.util;

import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.kh.evision.token.model.dao.TokenMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {
    
	private final TokenMapper tokenMapper;
	
    @Value("${jwt.secret}")
    private String secretKey;
    
    private SecretKey key;
    
    @PostConstruct
    public void init() {
        byte[] arr = Base64.getDecoder().decode(secretKey);
        this.key = Keys.hmacShaKeyFor(arr);
    }
    
    public String getAccessToken(String memberNo) {
    	
    	return Jwts.builder()
    			   .subject(memberNo) // 사용자 번호
    			   .issuedAt(new Date()) // 발급일
    			   .expiration(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24))) // 만료일
    			   .signWith(key) // 서명
    			   .compact();
    	
    	}
    	
    	public String getRefreshToken(String memberNo) {
    		return Jwts.builder()
    				   .subject(memberNo)
    				   .issuedAt(new Date()) 
    				   .expiration(Date.from(Instant.now().plus(Duration.ofDays(3))))
    				   .signWith(key)
    				   .compact();
    	}
    	
    	public Claims parseJwt(String token) {
    		return Jwts.parser()
    				   .verifyWith(key)
    				   .build()
    				   .parseSignedClaims(token)
    				   .getPayload();
    	}

    
}