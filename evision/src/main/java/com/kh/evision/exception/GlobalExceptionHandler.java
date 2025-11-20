package com.kh.evision.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.kh.evision.exception.custom.member.CustomAuthenticationException;
import com.kh.evision.exception.custom.member.IdDuplicateException;
import com.kh.evision.exception.custom.member.LoginFailException;
import com.kh.evision.exception.custom.member.NicknameDuplicateException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	private ResponseEntity< Map<String, String>> createResponseEntity(RuntimeException e, HttpStatus status) {
		Map<String, String> error = new HashMap();
		error.put("error-message", e.getMessage());
		return ResponseEntity.status(status).body(error);
	}
	@ExceptionHandler(CustomAuthenticationException.class)
	public ResponseEntity<Map<String, String>> handleAuth(CustomAuthenticationException e){
			return createResponseEntity(e, HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(IdDuplicateException.class)
	public ResponseEntity<?> handlerDuplicateId(IdDuplicateException e){
		Map<String, String> error = new HashMap();
		error.put("error-message", e.getMessage());
		return ResponseEntity.badRequest().body(error);
		
	}
	@ExceptionHandler(NicknameDuplicateException.class)
	public ResponseEntity<?> handlerDuplicateNickname(NicknameDuplicateException e){
		Map<String, String> error = new HashMap();
		error.put("error-message", e.getMessage());
		return ResponseEntity.badRequest().body(error);
	}
	
	//이거 좀 이상한데...
	@ExceptionHandler(LoginFailException.class)
	public ResponseEntity<?> handlerLoginFailException(LoginFailException e){
		Map<String, String> error = new HashMap();
		error.put("error-message", e.getMessage());
		return ResponseEntity.badRequest().body(error);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> hendlerArgumentsNotValid(MethodArgumentNotValidException e){
		/*
		List<FieldError> list = e.getBindingResult().getFieldErrors();
		for(int i = 0; i< list.size(); i++) {
			log. info("예외 발생 필드명 : {}, 발생한 이유 : {}",
					list.get(i).getField(),
					list.get(i).getDefaultMessage());
		}
		*/
		
		Map<String, String> errors = new HashMap();
		e.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
		return ResponseEntity.badRequest().body(errors);
	}
	
	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<?> handlerUsernameNotFound(UsernameNotFoundException e){
		Map<String, String> error = new HashMap();
		error.put("error-message", e.getMessage());
		return ResponseEntity.badRequest().body(error);
	}

}
