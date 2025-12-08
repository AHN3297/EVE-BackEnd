package com.kh.evision.exception.custom.car;

public class CarAlreadyReservedException extends RuntimeException {
	
	public CarAlreadyReservedException(String message) {
		super(message);
	}

}
