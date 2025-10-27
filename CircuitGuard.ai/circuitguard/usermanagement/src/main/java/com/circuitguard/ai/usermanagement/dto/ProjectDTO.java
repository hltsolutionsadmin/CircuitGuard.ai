package com.circuitguard.ai.usermanagement.dto;

import com.circuitguard.ai.usermanagement.dto.enums.ProjectStatus;
import com.circuitguard.ai.usermanagement.dto.enums.ProjectType;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectDTO {

    private Long id;

    @NotBlank(message = "Project name is required")
    private String name;

    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;

    @NotNull(message = "Client user ID is required")
    private Long clientId;

    private Long projectManagerId;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDate targetEndDate;

    private LocalDate dueDate;

    private ProjectStatus status = ProjectStatus.PLANNED;

    private ProjectType type;

    @NotNull(message = "Owner organization ID is required")
    private Long ownerOrganizationId;

    private Long clientOrganizationId;

    @Min(value = 0, message = "Progress percentage must be at least 0")
    @Max(value = 100, message = "Progress percentage cannot exceed 100")
    private int progressPercentage = 0;

    private String budgetRange;

    private String expectedTeamSize;

    private Boolean archived = false;

    private List<UserAssignmentDTO> projectMembers;

    private List<ProjectTechDTO> technologyStack;
}
