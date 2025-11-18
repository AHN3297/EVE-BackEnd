package com.kh.evision.token.member.vo;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenVO {
    
    private String token;           // JWT 토큰 문자열
    private Long memberNo;          // 회원 번호
    private LocalDateTime expiration; // 만료 시간
    
    /**
     * 토큰 만료 여부 확인
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiration);
    }
}