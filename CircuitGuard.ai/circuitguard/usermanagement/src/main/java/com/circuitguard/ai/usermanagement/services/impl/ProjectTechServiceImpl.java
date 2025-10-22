
package com.circuitguard.ai.usermanagement.services.impl;

import com.circuitguard.ai.usermanagement.dto.ProjectTechDTO;
import com.circuitguard.ai.usermanagement.model.ProjectModel;
import com.circuitguard.ai.usermanagement.model.ProjectTechModel;
import com.circuitguard.ai.usermanagement.populator.ProjectTechPopulator;
import com.circuitguard.ai.usermanagement.repository.ProjectRepository;
import com.circuitguard.ai.usermanagement.repository.ProjectTechRepository;
import com.circuitguard.ai.usermanagement.services.ProjectTechService;
import com.circuitguard.auth.exception.handling.ErrorCode;
import com.circuitguard.auth.exception.handling.HltCustomerException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectTechServiceImpl implements ProjectTechService {

    private final ProjectTechRepository projectTechRepository;
    private final ProjectRepository projectRepository;
    private final ProjectTechPopulator projectTechPopulator;

    @Override
    public ProjectTechDTO addTechToProject(Long projectId, ProjectTechDTO dto) {
        ProjectModel project = projectRepository.findById(projectId)
                .orElseThrow(() -> new HltCustomerException(ErrorCode.PROJECT_NOT_FOUND));

        boolean exists = projectTechRepository.existsByProjectIdAndTechnologyNameIgnoreCase(projectId, dto.getTechnologyName());
        if (exists) {
            throw new HltCustomerException(ErrorCode.DUPLICATE_ENTRY);
        }

        ProjectTechModel model = new ProjectTechModel();
        model.setProject(project);
        model.setTechnologyName(dto.getTechnologyName());
        model.setVersion(dto.getVersion());

        ProjectTechModel saved = projectTechRepository.save(model);

        ProjectTechDTO response = new ProjectTechDTO();
        projectTechPopulator.populate(saved, response);

        return response;
    }

    @Override
    public Page<ProjectTechDTO> getTechStackByProject(Long projectId, Pageable pageable) {
        Page<ProjectTechModel> page = projectTechRepository.findByProjectId(projectId, pageable);
        return page.map(model -> {
            ProjectTechDTO dto = new ProjectTechDTO();
            projectTechPopulator.populate(model, dto);
            dto.setProjectId(model.getProject().getId());
            return dto;
        });
    }

    @Override
    public ProjectTechDTO updateTech(Long techId, ProjectTechDTO dto) {
        ProjectTechModel existing = projectTechRepository.findById(techId)
                .orElseThrow(() -> new HltCustomerException(ErrorCode.TECH_STACK_NOT_FOUND));

        Optional.ofNullable(dto.getTechnologyName()).ifPresent(existing::setTechnologyName);
        Optional.ofNullable(dto.getVersion()).ifPresent(existing::setVersion);

        ProjectTechModel updated = projectTechRepository.save(existing);

        ProjectTechDTO response = new ProjectTechDTO();
        projectTechPopulator.populate(updated, response);
        response.setProjectId(updated.getProject() != null ? updated.getProject().getId() : null);

        return response;
    }

    @Override
    public void deleteTech(Long techId) {
        if (!projectTechRepository.existsById(techId)) {
            throw new HltCustomerException(ErrorCode.TECH_STACK_NOT_FOUND);
        }
        projectTechRepository.deleteById(techId);
    }
}
