package com.circuitguard.ai.usermanagement.dto;

import com.circuitguard.ai.usermanagement.dto.enums.ProjectStatus;
import com.circuitguard.ai.usermanagement.dto.enums.ProjectType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {

    private Long id;

    @NotBlank(message = "Project name is required")
    @Size(max = 255, message = "Project name cannot exceed 255 characters")
    private String name;

    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;

    @NotNull(message = "Client ID is required")
    private Long clientId;

    private Long projectManagerId;

    private LocalDate startDate;
    private LocalDate endDate;

    @NotNull(message = "Project status is required")
    private ProjectStatus status;

    private ProjectType type;

    private Set<@NotNull(message = "Team member ID cannot be null") Long> teamMemberIds;

    private List<@NotNull(message = "Ticket ID cannot be null") Long> ticketIds;

    private List<ProjectTechDTO> technologyStack;

    @Min(value = 0, message = "Progress must be at least 0")
    @Max(value = 100, message = "Progress cannot exceed 100")
    private int progressPercentage;

    @Size(max = 50, message = "Budget range cannot exceed 50 characters")
    private String budgetRange;

    @Size(max = 50, message = "Expected team size cannot exceed 50 characters")
    private String expectedTeamSize;

    private LocalDate targetEndDate;
    private LocalDate dueDate;

    @NotNull
    private Boolean archived;
}
