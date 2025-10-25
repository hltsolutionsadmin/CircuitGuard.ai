package com.circuitguard.ai.usermanagement.dto;

import com.circuitguard.ai.usermanagement.dto.enums.AssignmentRole;
import com.circuitguard.ai.usermanagement.dto.enums.AssignmentTargetType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

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

    @NotNull(message = "Role cannot be null")
    private AssignmentRole role;

    @NotNull(message = "Target ID is required")
    private Long targetId;

    private Set<Long> groupIds;


    private Boolean active;

    private String username;
    private String fullName;
    private String primaryContact;
    private String password;
    private String email;
}
