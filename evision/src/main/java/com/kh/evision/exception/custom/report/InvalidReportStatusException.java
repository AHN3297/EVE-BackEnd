package com.kh.evision.exception.custom.report;

public class InvalidReportStatusException extends RuntimeException {
    
    public InvalidReportStatusException(String message) {
        super(message);
    }
    
    public InvalidReportStatusException() {
        super("유효하지 않은 신고 상태입니다.");
    }
}

