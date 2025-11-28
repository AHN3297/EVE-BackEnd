package com.kh.evision.token.model.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.kh.evision.token.model.vo.RefreshToken;

@Mapper
public interface TokenMapper {
	@Insert("INSERT INTO TB_TOKEN VALUES(#{token}, #{username}, #{expiration})")
	int saveToken(RefreshToken token);
	
	@Delete("DELETE FROM TB_TOKEN WHERE MEMBER_ID = #{memberId}")
	void deleteToken(String memberId);

	@Select("SELECT TOKEN, MEMBER_ID username, EXPIRATION FROM TB_TOKEN WHERE TOKEN = #{refreshToken}")
	RefreshToken findByToken(String refreshToken);
    
}