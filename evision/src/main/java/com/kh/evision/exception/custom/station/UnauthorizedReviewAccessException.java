package com.kh.evision.exception.custom.station;

public class UnauthorizedReviewAccessException extends RuntimeException {
    
    public UnauthorizedReviewAccessException(String message) {
        super(message);
    }
    
    public UnauthorizedReviewAccessException() {
        super("해당 리뷰에 대한 권한이 없습니다.");
    }
}

