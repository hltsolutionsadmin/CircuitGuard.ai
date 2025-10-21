package com.circuitguard.ai.usermanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectTechDTO {
    private Long id;

    @NotBlank(message = "Technology name is required")
    private String technologyName;

    @Size(max = 50)
    private String version;
}
