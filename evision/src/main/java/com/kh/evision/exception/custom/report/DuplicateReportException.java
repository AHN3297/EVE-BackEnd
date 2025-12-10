package com.kh.evision.exception.custom.report;

public class DuplicateReportException extends RuntimeException {
    
    public DuplicateReportException(String message) {
        super(message);
    }
    
    public DuplicateReportException(Long boardNo) {
        super("이미 해당 게시글을 신고하셨습니다. 게시글 번호: " + boardNo);
    }
}

