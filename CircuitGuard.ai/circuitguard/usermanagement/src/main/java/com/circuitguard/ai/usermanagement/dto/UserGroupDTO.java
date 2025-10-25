package com.circuitguard.ai.usermanagement.dto;

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
}
