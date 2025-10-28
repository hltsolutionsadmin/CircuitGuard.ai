package com.circuitguard.ai.usermanagement.dto;

import com.circuitguard.ai.usermanagement.dto.enums.AssignmentRole;
import com.circuitguard.ai.usermanagement.dto.enums.AssignmentTargetType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAssignmentDTO {

    @NotEmpty(message = "User IDs are required")
    private List<Long> userIds = new ArrayList<>();

    @NotNull(message = "Target type is required")
    private AssignmentTargetType targetType;

    @NotEmpty(message = "At least one role must be provided")
    private List<AssignmentRole> roles = new ArrayList<>();

    @NotNull(message = "Target ID is required")
    private Long targetId;

    private List<Long> groupIds = new ArrayList<>();

    private Boolean active;

    private String username;
    private String fullName;
    private String primaryContact;
    private String password;
    private String email;
}
