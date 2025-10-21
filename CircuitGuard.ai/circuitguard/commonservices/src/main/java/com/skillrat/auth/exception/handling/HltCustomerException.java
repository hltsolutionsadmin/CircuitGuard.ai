package com.skillrat.auth.exception.handling;

import lombok.Getter;

@Getter
public class HltCustomerException extends RuntimeException {
    private final ErrorCode errorCode;

    public HltCustomerException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public HltCustomerException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

}
