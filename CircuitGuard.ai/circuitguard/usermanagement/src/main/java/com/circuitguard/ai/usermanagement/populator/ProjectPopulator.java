package com.circuitguard.ai.usermanagement.populator;

import com.circuitguard.ai.usermanagement.dto.ProjectDTO;
import com.circuitguard.ai.usermanagement.dto.ProjectTechDTO;
import com.circuitguard.ai.usermanagement.dto.UserAssignmentDTO;
import com.circuitguard.ai.usermanagement.model.ProjectModel;
import com.circuitguard.utils.Populator;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ProjectPopulator implements Populator<ProjectModel, ProjectDTO> {

    private final UserAssignmentPopulator userAssignmentPopulator;
    private final ProjectTechPopulator projectTechPopulator;

    public ProjectPopulator(UserAssignmentPopulator userAssignmentPopulator,
                            ProjectTechPopulator projectTechPopulator) {
        this.userAssignmentPopulator = userAssignmentPopulator;
        this.projectTechPopulator = projectTechPopulator;
    }

    @Override
    public void populate(ProjectModel source, ProjectDTO target) {
        if (source == null || target == null) {
            return;
        }

        // Basic fields
        target.setId(source.getId());
        target.setName(source.getName());
        target.setProjectCode(source.getProjectCode());
        target.setDescription(source.getDescription());
        target.setStartDate(source.getStartDate());
        target.setEndDate(source.getEndDate());
        target.setTargetEndDate(source.getTargetEndDate());
        target.setDueDate(source.getDueDate());
        target.setStatus(source.getStatus());
        target.setType(source.getType());
        target.setProgressPercentage(source.getProgressPercentage());
        target.setBudgetRange(source.getBudgetRange());
        target.setExpectedTeamSize(source.getExpectedTeamSize());
        target.setArchived(source.getArchived());

        // References
        if (source.getClient() != null) {
            target.setClientId(source.getClient().getId());
        }
        if (source.getProjectManager() != null) {
            target.setProjectManagerId(source.getProjectManager().getId());
        }
        if (source.getOwnerOrganization() != null) {
            target.setOwnerOrganizationId(source.getOwnerOrganization().getId());
        }
        if (source.getClientOrganization() != null) {
            target.setClientOrganizationId(source.getClientOrganization().getId());
        }

        // Map Project Members (UserAssignments)
        if (source.getUserAssignments() != null) {
            target.setProjectMembers(
                    source.getUserAssignments().stream()
                            .map(ua -> {
                                UserAssignmentDTO dto = new UserAssignmentDTO();
                                userAssignmentPopulator.populate(ua, dto);
                                return dto;
                            })
                            .collect(Collectors.toList())
            );
        }

        // Map Technology Stack
        if (source.getTechnologyStack() != null) {
            target.setTechnologyStack(
                    source.getTechnologyStack().stream()
                            .map(pt -> {
                                ProjectTechDTO dto = new ProjectTechDTO();
                                projectTechPopulator.populate(pt, dto);
                                return dto;
                            })
                            .collect(Collectors.toList())
            );
        }
    }

    public ProjectDTO toDTO(ProjectModel source) {
        ProjectDTO dto = new ProjectDTO();
        populate(source, dto);
        return dto;
    }
}
