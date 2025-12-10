package com.kh.evision.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.kh.evision.exception.custom.car.CarAlreadyReservedException;
import com.kh.evision.exception.custom.car.CarNotAvailableException;
import com.kh.evision.exception.custom.car.CarNotFoundException;
import com.kh.evision.exception.custom.member.AdminException;
import com.kh.evision.exception.custom.member.CustomAuthenticationException;
import com.kh.evision.exception.custom.member.IdDuplicateException;
import com.kh.evision.exception.custom.member.LoginFailException;
import com.kh.evision.exception.custom.member.NicknameDuplicateException;
<<<<<<< HEAD
import com.kh.evision.exception.custom.report.DuplicateReportException;
import com.kh.evision.exception.custom.report.InvalidReportStatusException;
import com.kh.evision.exception.custom.report.ReportNotFoundException;
import com.kh.evision.exception.custom.station.ReviewNotFoundException;
import com.kh.evision.exception.custom.station.StationNotFoundException;
import com.kh.evision.exception.custom.station.UnauthorizedReviewAccessException;
=======
import com.kh.evision.exception.custom.member.NoMatchPasswordException;
import com.kh.evision.exception.custom.member.NoPasswordException;
import com.kh.evision.exception.custom.member.NotUserException;
import com.kh.evision.exception.custom.member.RoleException;
import com.kh.evision.exception.custom.member.StatusException;
>>>>>>> eeb0645401f4e1a09508e168556b92f980509291

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
	@ExceptionHandler(RoleException.class)
	public ResponseEntity<?> handlerRole(RoleException e){
		Map<String, String> error = new HashMap();
		error.put("error-message", e.getMessage());
		return ResponseEntity.badRequest().body(error);
	}
	@ExceptionHandler(StatusException.class)
	public ResponseEntity<?> handlerStatus(StatusException e){
		Map<String, String> error = new HashMap();
		error.put("error-message", e.getMessage());
		return ResponseEntity.badRequest().body(error);
	}
	@ExceptionHandler(NoPasswordException.class)
	public ResponseEntity<?> handlerNoPassword(NoPasswordException e){
		Map<String, String> error = new HashMap();
		error.put("error-message", e.getMessage());
		return ResponseEntity.badRequest().body(error);
	}
	@ExceptionHandler(NoMatchPasswordException.class)
	public ResponseEntity<?> hanlderNoMatchPassword(NoMatchPasswordException e){
		Map<String, String> error = new HashMap();
		error.put("error-message", e.getMessage());
		return ResponseEntity.badRequest().body(error);
	}
	@ExceptionHandler(AdminException.class)
	public ResponseEntity<?> handlerAdmin(AdminException e){
		return createResponseEntity(e, HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(FileUploadFailureException.class)
	public ResponseEntity<Map<String, String>> handleFileUploadFailure(FileUploadFailureException e) {
		return createResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(InvalidImgFormatException.class)
	public ResponseEntity<Map<String, String>> handleInvalidImgFormat(InvalidImgFormatException e) {
		return createResponseEntity(e, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
	}
	
	@ExceptionHandler(InvalidParameterException.class)
	public ResponseEntity<Map<String, String>> handleInvalidParameter(InvalidParameterException e) {
		return createResponseEntity(e, HttpStatus.BAD_REQUEST);
	}
	
<<<<<<< HEAD
	// ==================== Station 관련 예외 핸들러 ====================
	
	@ExceptionHandler(StationNotFoundException.class)
	public ResponseEntity<Map<String, String>> handleStationNotFound(StationNotFoundException e) {
		log.warn("충전소를 찾을 수 없음: {}", e.getMessage());
		return createResponseEntity(e, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ReviewNotFoundException.class)
	public ResponseEntity<Map<String, String>> handleReviewNotFound(ReviewNotFoundException e) {
		log.warn("리뷰를 찾을 수 없음: {}", e.getMessage());
		return createResponseEntity(e, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(UnauthorizedReviewAccessException.class)
	public ResponseEntity<Map<String, String>> handleUnauthorizedReviewAccess(UnauthorizedReviewAccessException e) {
		log.warn("리뷰 접근 권한 없음: {}", e.getMessage());
		return createResponseEntity(e, HttpStatus.FORBIDDEN);
	}
	
	// ==================== Report 관련 예외 핸들러 ====================
	
	@ExceptionHandler(ReportNotFoundException.class)
	public ResponseEntity<Map<String, String>> handleReportNotFound(ReportNotFoundException e) {
		log.warn("신고를 찾을 수 없음: {}", e.getMessage());
		return createResponseEntity(e, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(DuplicateReportException.class)
	public ResponseEntity<Map<String, String>> handleDuplicateReport(DuplicateReportException e) {
		log.warn("중복 신고: {}", e.getMessage());
		return createResponseEntity(e, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(InvalidReportStatusException.class)
	public ResponseEntity<Map<String, String>> handleInvalidReportStatus(InvalidReportStatusException e) {
		log.warn("유효하지 않은 신고 상태: {}", e.getMessage());
		return createResponseEntity(e, HttpStatus.BAD_REQUEST);
=======
<<<<<<< HEAD
	@ExceptionHandler(CarNotFoundException.class)
	public ResponseEntity<Map<String, String>> handleCarNotFound(CarNotFoundException e) {
		return createResponseEntity(e, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(CarAlreadyReservedException.class)
	public ResponseEntity<Map<String, String>> handleCarAlreadyReservedException(CarAlreadyReservedException e) {
		return createResponseEntity(e, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(CarNotAvailableException.class)
	public ResponseEntity<Map<String, String>> handleCarNotAvailableException(CarNotAvailableException e) {
		return createResponseEntity(e, HttpStatus.BAD_REQUEST);
	
=======
	@ExceptionHandler(NotUserException.class)
	public ResponseEntity<String> handleNotUser(NotUserException e) {
		 return ResponseEntity.badRequest().body(e.getMessage());
>>>>>>> 4d5278c5e89a6f008248e912fb4a236e2d72d21a
>>>>>>> eeb0645401f4e1a09508e168556b92f980509291
	}
	
}
