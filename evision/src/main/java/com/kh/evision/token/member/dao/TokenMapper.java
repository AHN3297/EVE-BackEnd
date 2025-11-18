package com.kh.evision.token.member.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kh.evision.token.member.vo.TokenVO;

@Mapper
public interface TokenMapper {
    
    /**
     * 토큰 저장
     */
    int insertToken(TokenVO token);
    
    /**
     * 토큰으로 조회
     */
    TokenVO selectTokenByToken(@Param("token") String token);
    
    /**
     * 회원 번호로 토큰 조회
     */
    TokenVO selectTokenByMemberNo(@Param("memberNo") Long memberNo);
    
    /**
     * 토큰 삭제 (로그아웃)
     */
    int deleteToken(@Param("token") String token);
    
    /**
     * 회원의 모든 토큰 삭제
     */
    int deleteTokenByMemberNo(@Param("memberNo") Long memberNo);
    
    /**
     * 만료된 토큰 일괄 삭제
     */
    int deleteExpiredTokens();
    
    /**
     * 토큰 갱신 (같은 회원의 기존 토큰 삭제 후 새 토큰 저장)
     */
    int updateToken(TokenVO token);
}