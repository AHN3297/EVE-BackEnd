package com.kh.evision.exception.custom.member;

public class NoMatchPasswordException extends RuntimeException{
	
	public NoMatchPasswordException(String message) {
		super(message);
	}

}
