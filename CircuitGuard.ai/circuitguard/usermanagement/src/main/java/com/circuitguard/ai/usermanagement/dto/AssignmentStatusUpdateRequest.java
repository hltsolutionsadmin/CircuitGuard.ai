package com.circuitguard.ai.usermanagement.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignmentStatusUpdateRequest {

    @NotNull(message = "Active status is required")
    private Boolean active;
}
