package com.kh.evision.token.member.vo;

import java.time.LocalDateTime;
import java.time.ZoneId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenVO {
    private String token;
    private Long memberNo;
    private LocalDateTime expiration;
    
    // 밀리초 단위로 변환
    public Long getExpirationTime() {
        return expiration.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
    
    // 만료 여부 확인
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiration);
    }
}