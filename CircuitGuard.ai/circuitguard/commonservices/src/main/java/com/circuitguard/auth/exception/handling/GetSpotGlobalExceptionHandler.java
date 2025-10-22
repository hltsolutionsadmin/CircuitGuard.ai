package com.circuitguard.auth.exception.handling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GetSpotGlobalExceptionHandler {

	@ExceptionHandler(GetSpotCustomerException.class)
	public ResponseEntity<ErrorResponse> handleNivaasCustomException(GetSpotCustomerException customException) {
		ErrorCode errorCode = customException.getErrorCode();
		ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), customException.getMessage());
		return new ResponseEntity<>(errorResponse, errorCode.getHttpStatus());
	}

}