package com.circuitguard.ai.usermanagement.dto;

import com.circuitguard.ai.usermanagement.dto.enums.AssignmentTargetType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAssignmentDTO {

    private Long id;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Target type is required")
    private AssignmentTargetType targetType;

    @NotNull(message = "Target ID is required")
    private Long targetId;

    private String role;

    private Boolean active;
}
