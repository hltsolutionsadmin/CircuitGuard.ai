package com.circuitguard.ai.usermanagement.populator;

import com.circuitguard.ai.usermanagement.dto.ProjectDTO;
import com.circuitguard.ai.usermanagement.model.ProjectModel;
import org.springframework.stereotype.Component;

@Component
public class ProjectPopulator {

    public ProjectDTO toDTO(ProjectModel source) {
        if (source == null) return null;

        return ProjectDTO.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .clientId(source.getClient() != null ? source.getClient().getId() : null)
                .projectManagerId(source.getProjectManager() != null ? source.getProjectManager().getId() : null)
                .startDate(source.getStartDate())
                .endDate(source.getEndDate())
                .status(source.getStatus())
                .type(source.getType())
                .progressPercentage(source.getProgressPercentage())
                .budgetRange(source.getBudgetRange())
                .expectedTeamSize(source.getExpectedTeamSize())
                .targetEndDate(source.getTargetEndDate())
                .dueDate(source.getDueDate())
                .archived(source.getArchived())
                .build();
    }
}
