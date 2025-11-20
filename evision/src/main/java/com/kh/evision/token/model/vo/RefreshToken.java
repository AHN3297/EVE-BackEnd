package com.kh.evision.token.model.vo;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class RefreshToken {
	private String token;
	private Long memberNo;
	private Long expiration;
	private String username;


}
