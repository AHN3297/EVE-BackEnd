package com.kh.evision.exception.custom.station;

public class ReviewNotFoundException extends RuntimeException {
    
    public ReviewNotFoundException(String message) {
        super(message);
    }
    
    public ReviewNotFoundException(Long reviewNo) {
        super("리뷰를 찾을 수 없습니다. 리뷰 번호: " + reviewNo);
    }
}

