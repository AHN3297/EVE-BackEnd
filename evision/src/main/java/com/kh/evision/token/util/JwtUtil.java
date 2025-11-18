package com.kh.evision.token.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.kh.evision.token.member.dao.TokenMapper;
import com.kh.evision.token.member.vo.TokenVO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {
    
	private final TokenMapper tokenMapper;
	
    @Value("${jwt.secret}")
    private String secret;
    
    private SecretKey secretKey;
    
    @PostConstruct
    public void init() {
        // Base64로 디코딩해서 256비트 이상 보장
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }


    /**
     * JWT 토큰 생성 및 DB 저장
     * @param memberNo 회원 번호
     * @return JWT 토큰 문자열
     */
    @Transactional
    public String generateToken(Long memberNo) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("memberNo", memberNo);
        
        String token = createToken(claims, memberNo.toString());
        
        // DB에 토큰 저장
        saveTokenToDatabase(token, memberNo);
        
        return token;
    }

    /**
     * 실제 토큰 생성 로직
     */
    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .claims(claims)                    // 추가 정보
                .subject(subject)                   // 회원 번호
                .issuedAt(now)                      // 발행 시간
                .expiration(expiration)             // 만료 시간
                .signWith(secretKey)                // 서명
                .compact();
    }
    /**
     * DB에 토큰 저장
     */
    private void saveTokenToDatabase(String token, Long memberNo) {
        Date expiration = getExpirationDateFromToken(token);
        LocalDateTime expirationDateTime = expiration.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        
        TokenVO tokenVO = TokenVO.builder()
                .token(token)
                .memberNo(memberNo)
                .expiration(expirationDateTime)
                .build();
        
        // 기존 토큰 삭제 후 새 토큰 저장 (한 사용자당 하나의 토큰만 유지)
        tokenMapper.updateToken(tokenVO);
    }

    /**
     * JWT 토큰 유효성 검증 (JWT 검증 + DB 확인)
     * @param token JWT 토큰
     * @return 유효하면 true, 아니면 false
     */
    public boolean validateToken(String token) {
        // 1. JWT 자체 유효성 검증
        if (!validateJwtToken(token)) {
            return false;
        }
        
        // 2. DB에 토큰 존재 여부 및 만료 확인
        return validateTokenInDatabase(token);
    }

    /**
     * JWT 토큰 자체 검증
     */
    private boolean validateJwtToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    /**
     * DB에서 토큰 유효성 확인
     */
    private boolean validateTokenInDatabase(String token) {
        try {
            TokenVO tokenVO = tokenMapper.selectTokenByToken(token);
            
            if (tokenVO == null) {
                log.warn("Token not found in database");
                return false;
            }
            
            if (tokenVO.isExpired()) {
                log.warn("Token is expired in database");
                // 만료된 토큰 삭제
                tokenMapper.deleteToken(token);
                return false;
            }
            
            return true;
        } catch (Exception e) {
            log.error("Error validating token in database: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 토큰에서 회원 번호 추출
     * @param token JWT 토큰
     * @return 회원 번호
     */
    public Long getMemberNoFromToken(String token) {
        Claims claims = extractClaims(token);
        return claims.get("memberNo", Long.class);
    }

    /**
     * 토큰에서 사용자명(회원번호) 추출 (JwtFilter 호환용)
     * @param token JWT 토큰
     * @return 사용자명 (회원번호 문자열)
     */
    public String getUsernameFromToken(String token) {
        return getMemberNoFromToken(token).toString();
    }

    /**
     * 토큰 만료일 추출
     */
    public Date getExpirationDateFromToken(String token) {
        return extractClaims(token).getExpiration();
    }

    /**
     * 토큰에서 모든 Claims 추출
     */
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 토큰 무효화 (로그아웃)
     * @param token JWT 토큰
     */
    @Transactional
    public void invalidateToken(String token) {
        tokenMapper.deleteToken(token);
    }

    /**
     * 회원의 모든 토큰 무효화
     * @param memberNo 회원 번호
     */
    @Transactional
    public void invalidateAllTokensByMemberNo(Long memberNo) {
        tokenMapper.deleteTokenByMemberNo(memberNo);
    }

    /**
     * 만료된 토큰 정리 (스케줄러에서 사용)
     */
    @Transactional
    public int cleanupExpiredTokens() {
        return tokenMapper.deleteExpiredTokens();
    }
}