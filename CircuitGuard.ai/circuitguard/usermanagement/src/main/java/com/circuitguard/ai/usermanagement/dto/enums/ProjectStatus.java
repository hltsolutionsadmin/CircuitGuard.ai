package com.circuitguard.ai.usermanagement.dto.enums;

import org.springframework.http.HttpStatus;

public enum ProjectStatus {
    PLANNED("Planned", HttpStatus.OK),
    IN_PROGRESS("In Progress", HttpStatus.OK),
    ON_HOLD("On Hold", HttpStatus.OK),
    COMPLETED("Completed", HttpStatus.OK),
    CANCELLED("Cancelled", HttpStatus.OK);

    private final String displayName;
    private final HttpStatus httpStatus;

    ProjectStatus(String displayName, HttpStatus httpStatus) {
        this.displayName = displayName;
        this.httpStatus = httpStatus;
    }

    public String getDisplayName() {
        return displayName;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
