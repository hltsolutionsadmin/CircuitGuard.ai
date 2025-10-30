package com.circuitguard.ai.usermanagement.dto;

import com.circuitguard.ai.usermanagement.dto.enums.TicketPriority;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserGroupDTO {

    private Long id;

    @NotBlank(message = "Group name is required")
    private String groupName;

    private String description;

    private ProjectDTO project;

    private UserDTO groupLead;

    private TicketPriority priority;
}
