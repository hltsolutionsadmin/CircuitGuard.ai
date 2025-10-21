package com.circuitguard.ai.usermanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationDTO {

    private Long id;

    @NotBlank(message = "Organization name is required")
    @Size(max = 150, message = "Organization name must be at most 150 characters")
    private String name;

    @Size(max = 1000, message = "Description must be at most 1000 characters")
    private String description;

    @Size(max = 100, message = "Domain name must be at most 100 characters")
    private String domainName;

    private Boolean active = true;
}
