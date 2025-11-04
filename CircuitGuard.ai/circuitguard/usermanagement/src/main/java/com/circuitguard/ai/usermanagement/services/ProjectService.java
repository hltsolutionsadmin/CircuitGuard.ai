package com.circuitguard.ai.usermanagement.services;

import com.circuitguard.ai.usermanagement.dto.ProjectDTO;
import com.circuitguard.ai.usermanagement.dto.ProjectStatsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectService {

    ProjectDTO saveOrUpdateProject(ProjectDTO projectDTO);

    ProjectDTO getProjectById(Long projectId);

    Page<ProjectDTO> fetchProjectsWithFilters(Pageable pageable, Long organisationId, Long clientId, Long managerId, String statusStr);

    void deleteProject(Long projectId);

    Page<ProjectDTO> getProjectsForOrganization(Long organizationId, Pageable pageable);

    ProjectStatsDTO getProjectStats();

    Page<ProjectDTO> getProjectsForCurrentUser(Pageable pageable);

}
