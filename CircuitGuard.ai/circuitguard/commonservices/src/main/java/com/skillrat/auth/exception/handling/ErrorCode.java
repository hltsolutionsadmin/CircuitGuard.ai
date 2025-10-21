package com.skillrat.auth.exception.handling;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // ===========================
    // User & Auth Errors (1000–1099)
    // ===========================
    BUSINESS_VALIDATION_FAILED(1002, "Business validation failed", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1000, "User Not Found", HttpStatus.NOT_FOUND),
    SKILL_NOT_FOUND(2001, "Skill not found", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS(1001, "User Already Exists", HttpStatus.CONFLICT),
    EMAIL_ALREADY_IN_USE(1002, "Email Is Already In Use", HttpStatus.CONFLICT),
    UNAUTHORIZED(1003, "Unauthorized Access", HttpStatus.UNAUTHORIZED),
    UNSUPPORTED_MAPPING_TYPE(1021, "Unsupported mapping type for the given role", HttpStatus.BAD_REQUEST),
    MAPPING_ALREADY_DEACTIVATED(1002, "User mapping is already deactivated", HttpStatus.UNPROCESSABLE_ENTITY),
    BUSINESS_NOT_FOUND(1002, "Business not found", HttpStatus.NOT_FOUND),
    BUSINESS_CODE_ALREADY_EXISTS(1003, "Business code already exists", HttpStatus.CONFLICT),
    MAPPING_NOT_FOUND(1003, "Mapping not found", HttpStatus.NOT_FOUND),
    TELECALLER_MAPPING_LIMIT_EXCEEDED(1005, "Telecaller cannot be mapped to more than 2 hospitals", HttpStatus.CONFLICT),
    INVALID_ROLE(1007, "Invalid role provided", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED_OPERATION(1016, "Unauthorized operation", HttpStatus.UNAUTHORIZED),
    PROFILE_INCOMPLETE(1010, "Student profile not completed. Please complete your profile before applying.", HttpStatus.BAD_REQUEST),
    INVALID_ROLE_FOR_OPERATION(1017, "Invalid role for this operation", HttpStatus.FORBIDDEN),
    HOSPITAL_ADMIN_ALREADY_EXISTS(2001, "A hospital admin already exists for this hospital", HttpStatus.CONFLICT),
    REQUIREMENT_NOT_FOUND(2001, "Requirement not found", HttpStatus.NOT_FOUND),
    REQUIREMENT_ALREADY_EXISTS(2002, "Requirement already exists", HttpStatus.CONFLICT),
    REQUIREMENT_INVALID_DATA(2003, "Invalid requirement data", HttpStatus.BAD_REQUEST),
    REWARD_NOT_FOUND(3000, "User reward balance not found", HttpStatus.NOT_FOUND),
    INSUFFICIENT_POINTS(3001, "Not enough reward points available", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED_ROLE_ASSIGNMENT(1006, "You are not allowed to assign this role", HttpStatus.FORBIDDEN),
    INVALID_BUSINESS_TYPE(1003, "Invalid Business Type code", HttpStatus.BAD_REQUEST),
    APPLICATION_NOT_FOUND(3001, "Application not found", HttpStatus.NOT_FOUND),
    APPLICATION_ALREADY_EXISTS(3002, "Application already exists for this requirement and applicant", HttpStatus.CONFLICT),
    APPLICATION_INVALID_STATUS(3003, "Invalid application status", HttpStatus.BAD_REQUEST),
    APPLICATION_UNAUTHORIZED_ACCESS(3004, "You are not authorized to access this application", HttpStatus.FORBIDDEN),
    BUSINESS_ALREADY_APPROVED(2003, "Business already approved", HttpStatus.BAD_REQUEST),
    PROFILE_NOT_COMPLETED(1010, "Profile must be completed to perform this action", HttpStatus.FORBIDDEN),
    EDUCATION_NOT_FOUND(2001, "Education not found", HttpStatus.NOT_FOUND),
    INTERNSHIP_NOT_FOUND(2002, "Internship not found", HttpStatus.NOT_FOUND),
    JOB_NOT_FOUND(2003, "Job not found", HttpStatus.NOT_FOUND),
    // ===========================
    // OTP & Token (1800–1899)
    // ===========================
    OTP_EXPIRED(1801, "OTP Expired", HttpStatus.BAD_REQUEST),
    TOKEN_PROCESSING_ERROR(1804, "Error Processing Refresh Token", HttpStatus.INTERNAL_SERVER_ERROR),
    AZURE_BLOB_UPLOAD_FAILED(4001, "Failed to upload file to Azure Blob Storage", HttpStatus.INTERNAL_SERVER_ERROR),

    // ===========================
    // Address & App Info (1900–1999)
    // ===========================
    ADDRESS_NOT_FOUND(1901, "Address not found.", HttpStatus.NOT_FOUND),
    INVALID_ADDRESS(1902, "Invalid address data or unauthorized access.", HttpStatus.BAD_REQUEST),
    ACCESS_DENIED(1903, "Access denied —  ownership mismatch for the given user ID.", HttpStatus.BAD_REQUEST),

    // ===========================
    // General Exceptions (2000–2099)
    // ===========================
    NOT_FOUND(2000, "Requested Resource Not Found", HttpStatus.NOT_FOUND),
    BAD_REQUEST(2000, "Bad Request", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR(2001, "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR),
    FORBIDDEN(2002, "Forbidden", HttpStatus.FORBIDDEN),
    METHOD_NOT_ALLOWED(2003, "Method Not Allowed", HttpStatus.METHOD_NOT_ALLOWED),
    NULL_POINTER(2004, "Null Pointer Exception", HttpStatus.BAD_REQUEST),
    USER_INPUT_INVALID(3001, "Invalid user input", HttpStatus.BAD_REQUEST),

    // Product, Category, Business (3000–3099)
    // ===========================
    CATEGORY_NOT_FOUND(3001, "Category not found", HttpStatus.NOT_FOUND),
    ALREADY_EXISTS(3003, "Resource already exists", HttpStatus.CONFLICT),
    ROLE_NOT_FOUND(3004, "Role not found", HttpStatus.CONFLICT);

    // ===========================
    // Fields
    // ===========================
    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String formatMessage(Object... args) {
        return String.format(message, args);
    }
}
