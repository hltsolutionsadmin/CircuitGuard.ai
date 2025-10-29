package com.circuitguard.ai.usermanagement.services.impl;

import com.circuitguard.ai.usermanagement.dto.ProjectDTO;
import com.circuitguard.ai.usermanagement.dto.ProjectStatsDTO;
import com.circuitguard.ai.usermanagement.dto.enums.ProjectStatus;
import com.circuitguard.ai.usermanagement.model.OrganizationModel;
import com.circuitguard.ai.usermanagement.model.ProjectModel;
import com.circuitguard.ai.usermanagement.model.UserModel;
import com.circuitguard.ai.usermanagement.populator.ProjectPopulator;
import com.circuitguard.ai.usermanagement.repository.OrganizationRepository;
import com.circuitguard.ai.usermanagement.repository.ProjectRepository;
import com.circuitguard.ai.usermanagement.repository.UserRepository;
import com.circuitguard.ai.usermanagement.services.ProjectService;
import com.circuitguard.ai.usermanagement.utils.ProjectCodeGenerator;
import com.circuitguard.auth.exception.handling.ErrorCode;
import com.circuitguard.auth.exception.handling.HltCustomerException;
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

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectPopulator projectPopulator;
    private final OrganizationRepository organizationRepository;
    private final ProjectCodeGenerator projectCodeGenerator;

    @Override
    @Transactional
    public ProjectDTO saveOrUpdateProject(ProjectDTO projectDTO) {
        ProjectModel model;

        if (projectDTO.getId() == null) {
            model = new ProjectModel();

            model.setProjectCode(generateUniqueProjectCode(projectDTO.getName()));

        } else {
            model = projectRepository.findById(projectDTO.getId())
                    .orElseThrow(() -> new HltCustomerException(ErrorCode.PROJECT_NOT_FOUND));

            if (projectDTO.getProjectCode() != null
                    && !model.getProjectCode().equals(projectDTO.getProjectCode())
                    && projectRepository.existsByProjectCode(projectDTO.getProjectCode())) {
                throw new HltCustomerException(ErrorCode.PROJECT_ALREADY_REGISTERED);
            }
        }

        mapDtoToModel(projectDTO, model);

        ProjectModel saved = projectRepository.save(model);

        return projectPopulator.toDTO(saved);
    }




    @Override
    public ProjectDTO getProjectById(Long projectId) {
        ProjectModel model = projectRepository.findById(projectId)
                .orElseThrow(() -> new HltCustomerException(ErrorCode.BUSINESS_NOT_FOUND));
        return projectPopulator.toDTO(model);
    }

    @Override
    public Page<ProjectDTO> getAllProjects(Pageable pageable, Long clientId, Long managerId, String status) {
        Page<ProjectModel> page = fetchProjectsWithFilters(pageable, clientId, managerId, status);
        List<ProjectDTO> dtos = page.getContent().stream()
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

    @Override
    public Page<ProjectDTO> getProjectsForOrganization(Long organizationId, Pageable pageable) {
        Page<ProjectModel> projectsPage = projectRepository.findByOrganization(organizationId, pageable);

        List<ProjectDTO> dtos = projectsPage.getContent().stream()
                .map(projectPopulator::toDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, projectsPage.getTotalElements());
    }

    @Override
    public ProjectStatsDTO getProjectStats() {
        return ProjectStatsDTO.builder()
                .totalProjects(projectRepository.countAllProjects())
                .activeProjects(projectRepository.countActiveProjects())
                .completedProjects(projectRepository.countCompletedProjects())
                .onHoldProjects(projectRepository.countOnHoldProjects())
                .build();
    }


    private ProjectModel mapDtoToModel(ProjectDTO dto, ProjectModel model) {

        if (dto.getName() != null) model.setName(dto.getName());
        if (dto.getDescription() != null) model.setDescription(dto.getDescription());
        if (dto.getStartDate() != null) model.setStartDate(dto.getStartDate());
        if (dto.getEndDate() != null) model.setEndDate(dto.getEndDate());
        if (dto.getTargetEndDate() != null) model.setTargetEndDate(dto.getTargetEndDate());
        if (dto.getDueDate() != null) model.setDueDate(dto.getDueDate());
        if (dto.getStatus() != null) model.setStatus(dto.getStatus());
        if (dto.getType() != null) model.setType(dto.getType());
        if (dto.getProgressPercentage() != null) model.setProgressPercentage(dto.getProgressPercentage());
        if (dto.getBudgetRange() != null) model.setBudgetRange(dto.getBudgetRange());
        if (dto.getExpectedTeamSize() != null) model.setExpectedTeamSize(dto.getExpectedTeamSize());
        if (dto.getArchived() != null) model.setArchived(dto.getArchived());

        if (dto.getClientId() != null) {
            model.setClient(getUserById(dto.getClientId(), ErrorCode.CLIENT_NOT_FOUND));
        }

        if (dto.getProjectManagerId() != null) {
            model.setProjectManager(getUserById(dto.getProjectManagerId(), ErrorCode.USER_NOT_FOUND));
        }

        if (dto.getOwnerOrganizationId() != null)
            model.setOwnerOrganization(getOrganizationById(dto.getOwnerOrganizationId(), ErrorCode.ORGANIZATION_NOT_FOUND));

        if (dto.getClientOrganizationId() != null)
            model.setClientOrganization(getOrganizationById(dto.getClientOrganizationId(), ErrorCode.CLIENT_ORGANIZATION_NOT_FOUND));

        return model;
    }
    private UserModel getUserById(Long userId, ErrorCode errorCode) {
        return userId == null ? null :
                userRepository.findById(userId)
                        .orElseThrow(() -> new HltCustomerException(errorCode));
    }

    private OrganizationModel getOrganizationById(Long orgId, ErrorCode errorCode) {
        if (orgId == null) return null;
        return organizationRepository.findById(orgId)
                .orElseThrow(() -> new HltCustomerException(errorCode));
    }


    private Page<ProjectModel> fetchProjectsWithFilters(Pageable pageable, Long clientId, Long managerId, String statusStr) {
        ProjectStatus status = null;
        if (statusStr != null) {
            try {
                status = ProjectStatus.valueOf(statusStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new HltCustomerException(ErrorCode.BUSINESS_VALIDATION_FAILED, "Invalid project status: " + statusStr);
            }
        }

        if (clientId != null && managerId != null && status != null) {
            return projectRepository.findByClientIdAndProjectManagerIdAndStatus(
                    clientId, managerId, status, pageable);
        } else if (clientId != null && managerId != null) {
            return projectRepository.findByClientIdAndProjectManagerId(clientId, managerId, pageable);
        } else if (clientId != null && status != null) {
            return projectRepository.findByClientIdAndStatus(clientId, status, pageable);
        } else if (managerId != null && status != null) {
            return projectRepository.findByProjectManagerIdAndStatus(managerId, status, pageable);
        } else if (clientId != null) {
            return projectRepository.findByClientId(clientId, pageable);
        } else if (managerId != null) {
            return projectRepository.findByProjectManagerId(managerId, pageable);
        } else if (status != null) {
            return projectRepository.findByStatus(status, pageable);
        } else {
            return projectRepository.findAll(pageable);
        }
    }


    private String generateUniqueProjectCode(String projectName) {
        final int MAX_RETRIES = 5;

        for (int i = 0; i < MAX_RETRIES; i++) {
            String candidate = projectCodeGenerator.generateCode(projectName);
            boolean exists = projectRepository.existsByProjectCode(candidate);

            if (!exists) {
                return candidate;
            }
        }

        throw new HltCustomerException(ErrorCode.PROJECT_CODE_GENERATION_FAILED);
    }


}
