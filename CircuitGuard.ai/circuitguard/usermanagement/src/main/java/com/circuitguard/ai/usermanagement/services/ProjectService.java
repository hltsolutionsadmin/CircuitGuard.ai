package com.circuitguard.ai.usermanagement.services;

import com.circuitguard.ai.usermanagement.dto.ProjectDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectService {

    ProjectDTO createProject(ProjectDTO projectDTO);

    ProjectDTO getProjectById(Long projectId);

    Page<ProjectDTO> getAllProjects(Pageable pageable, Long clientId, Long managerId, String status);

    void deleteProject(Long projectId);

    Page<ProjectDTO> getProjectsForOrganization(Long organizationId, Pageable pageable);

}
