package com.circuitguard.ai.usermanagement.services.impl;

import com.circuitguard.ai.usermanagement.dto.ProjectDTO;
import com.circuitguard.ai.usermanagement.dto.enums.ProjectStatus;
import com.circuitguard.ai.usermanagement.model.ProjectModel;
import com.circuitguard.ai.usermanagement.populator.ProjectPopulator;
import com.circuitguard.ai.usermanagement.repository.ProjectRepository;
import com.circuitguard.ai.usermanagement.repository.UserRepository;
import com.circuitguard.ai.usermanagement.services.ProjectService;
import com.skillrat.auth.exception.handling.ErrorCode;
import com.skillrat.auth.exception.handling.HltCustomerException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final  ProjectRepository projectRepository;

    private  final UserRepository userRepository;

    private  final ProjectPopulator projectPopulator;

    @Override
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        if (projectRepository.existsByName(projectDTO.getName())) {
            throw new HltCustomerException(ErrorCode.BUSINESS_VALIDATION_FAILED);
        }
        ProjectModel model = new ProjectModel();
        model.setName(projectDTO.getName());
        model.setDescription(projectDTO.getDescription());
        model.setStartDate(projectDTO.getStartDate());
        model.setEndDate(projectDTO.getEndDate());
        model.setStatus(projectDTO.getStatus());
        model.setType(projectDTO.getType());
        model.setProgressPercentage(projectDTO.getProgressPercentage());
        model.setBudgetRange(projectDTO.getBudgetRange());
        model.setExpectedTeamSize(projectDTO.getExpectedTeamSize());
        model.setTargetEndDate(projectDTO.getTargetEndDate());
        model.setDueDate(projectDTO.getDueDate());
        model.setArchived(projectDTO.getArchived());

        if (projectDTO.getClientId() != null) {
            model.setClient(userRepository.findById(projectDTO.getClientId())
                    .orElseThrow(() -> new HltCustomerException(ErrorCode.BUSINESS_NOT_FOUND)));
        }
        if (projectDTO.getProjectManagerId() != null) {
            model.setProjectManager(userRepository.findById(projectDTO.getProjectManagerId())
                    .orElseThrow(() -> new HltCustomerException(ErrorCode.BUSINESS_NOT_FOUND)));
        }

        ProjectModel saved = projectRepository.save(model);
        return projectPopulator.toDTO(saved);
    }

    @Override
    public ProjectDTO updateProject(Long projectId, ProjectDTO projectDTO) {
        ProjectModel model = projectRepository.findById(projectId)
                .orElseThrow(() -> new HltCustomerException(ErrorCode.BUSINESS_NOT_FOUND));

        model.setName(projectDTO.getName());
        model.setDescription(projectDTO.getDescription());
        model.setStartDate(projectDTO.getStartDate());
        model.setEndDate(projectDTO.getEndDate());
        model.setStatus(projectDTO.getStatus());
        model.setType(projectDTO.getType());
        model.setProgressPercentage(projectDTO.getProgressPercentage());
        model.setBudgetRange(projectDTO.getBudgetRange());
        model.setExpectedTeamSize(projectDTO.getExpectedTeamSize());
        model.setTargetEndDate(projectDTO.getTargetEndDate());
        model.setDueDate(projectDTO.getDueDate());
        model.setArchived(projectDTO.getArchived());

        ProjectModel updated = projectRepository.save(model);
        return projectPopulator.toDTO(updated);
    }

    @Override
    public ProjectDTO getProjectById(Long projectId) {
        ProjectModel model = projectRepository.findById(projectId)
                .orElseThrow(() -> new HltCustomerException(ErrorCode.BUSINESS_NOT_FOUND));
        return projectPopulator.toDTO(model);
    }

    @Override
    public Page<ProjectDTO> getAllProjects(Pageable pageable, Long clientId, Long managerId, String status) {
        Page<ProjectModel> page;

        if (clientId != null && managerId != null && status != null) {
            page = projectRepository.findByClientIdAndProjectManagerIdAndStatus(
                    clientId,
                    managerId,
                    ProjectStatus.valueOf(status),
                    pageable
            );
        } else if (clientId != null && managerId != null) {
            page = projectRepository.findByClientIdAndProjectManagerId(clientId, managerId, pageable);
        } else if (clientId != null && status != null) {
            page = projectRepository.findByClientIdAndStatus(clientId, ProjectStatus.valueOf(status), pageable);
        } else if (managerId != null && status != null) {
            page = projectRepository.findByProjectManagerIdAndStatus(managerId, ProjectStatus.valueOf(status), pageable);
        } else if (clientId != null) {
            page = projectRepository.findByClientId(clientId, pageable);
        } else if (managerId != null) {
            page = projectRepository.findByProjectManagerId(managerId, pageable);
        } else if (status != null) {
            page = projectRepository.findByStatus(ProjectStatus.valueOf(status), pageable);
        } else {
            page = projectRepository.findAll(pageable);
        }

        List<ProjectDTO> dtos = page.getContent()
                .stream()
                .map(projectPopulator::toDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }


    @Override
    public void deleteProject(Long projectId) {
        ProjectModel model = projectRepository.findById(projectId)
                .orElseThrow(() -> new HltCustomerException(ErrorCode.BUSINESS_NOT_FOUND));
        projectRepository.delete(model);
    }
}
