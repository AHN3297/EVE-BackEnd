package com.kh.evision.exception.custom.member;

public class CustomAuthenticationException extends RuntimeException {
	
	public CustomAuthenticationException (String message) {
		super(message);
	}
}
