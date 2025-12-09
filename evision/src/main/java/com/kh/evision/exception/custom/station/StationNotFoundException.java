package com.kh.evision.exception.custom.station;

public class StationNotFoundException extends RuntimeException {
    
    public StationNotFoundException(String message) {
        super(message);
    }
    
    public StationNotFoundException(Long stationNo) {
        super("충전소를 찾을 수 없습니다. 충전소 번호: " + stationNo);
    }
}

