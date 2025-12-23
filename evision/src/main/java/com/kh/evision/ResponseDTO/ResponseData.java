package com.kh.evision.ResponseDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Builder
public class ResponseData<T> {
	private String message;
	private Object data;
	private String success;
	
	private ResponseData(String message, Object data, String success) {
		this.message=message;
		this.data = data;
		this.success = success;
	}
	public static <T> ResponseEntity<ResponseData<T>> ok(Object data){
		return ResponseEntity.ok(new ResponseData<T>(null, data, "요청성공"));
	}
	
	public static <T> ResponseEntity<ResponseData<T>> ok(Object data, String message){
		return ResponseEntity.ok(new ResponseData<T>(message, data, "요청성공"));
	}
	
	public static <T> ResponseEntity<ResponseData<T>> created(Object data){
		return ResponseEntity.status(HttpStatus.CREATED)
				             .body(new ResponseData<T>("생성되었습니다.", data,"요청성공"));
	}
	
	public static <T> ResponseEntity<ResponseData<T>> badRequest(String message, HttpStatus status){
		return ResponseEntity.status(status).body(new ResponseData<T>(message, null, "요청실패"));
	}
	
}


