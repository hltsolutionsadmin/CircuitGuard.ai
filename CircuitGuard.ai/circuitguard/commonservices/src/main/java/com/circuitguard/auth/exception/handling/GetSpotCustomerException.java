package com.circuitguard.auth.exception.handling;

import lombok.Getter;

@Getter
public class GetSpotCustomerException extends RuntimeException {

	private final ErrorCode errorCode;

	public GetSpotCustomerException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	public GetSpotCustomerException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}
}
