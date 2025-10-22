package com.circuitguard.ai.usermanagement.services;

import com.circuitguard.ai.usermanagement.dto.ProjectTechDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectTechService {

    ProjectTechDTO addTechToProject(Long projectId, ProjectTechDTO dto);

    Page<ProjectTechDTO> getTechStackByProject(Long projectId, Pageable pageable);

    ProjectTechDTO updateTech(Long techId, ProjectTechDTO dto);

    void deleteTech(Long techId);
}
