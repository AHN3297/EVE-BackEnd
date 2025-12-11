package com.kh.evision.exception.custom.report;

public class ReportNotFoundException extends RuntimeException {
    
    public ReportNotFoundException(String message) {
        super(message);
    }
    
    public ReportNotFoundException(Long reportNo) {
        super("신고를 찾을 수 없습니다. 신고 번호: " + reportNo);
    }
}

