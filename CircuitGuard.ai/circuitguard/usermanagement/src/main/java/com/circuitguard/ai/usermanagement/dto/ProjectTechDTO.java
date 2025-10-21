package com.circuitguard.ai.usermanagement.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectTechDTO {

    private Long id;

    @NotBlank(message = "Technology name is required")
    private String tech;
}
