package com.circuitguard.ai.usermanagement.services.impl;

import com.circuitguard.ai.usermanagement.dto.ProjectDTO;
import com.circuitguard.ai.usermanagement.dto.enums.ProjectStatus;
import com.circuitguard.ai.usermanagement.model.OrganizationModel;
import com.circuitguard.ai.usermanagement.model.ProjectModel;
import com.circuitguard.ai.usermanagement.populator.ProjectPopulator;
import com.circuitguard.ai.usermanagement.repository.ProjectRepository;
import com.circuitguard.ai.usermanagement.repository.UserRepository;
import com.circuitguard.ai.usermanagement.services.ProjectService;
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

    @Override
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        if (projectRepository.existsByName(projectDTO.getName())) {
            throw new HltCustomerException(ErrorCode.BUSINESS_VALIDATION_FAILED);
        }

        ProjectModel model = mapDtoToModel(projectDTO, new ProjectModel());
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


    private ProjectModel mapDtoToModel(ProjectDTO dto, ProjectModel model) {
        model.setName(dto.getName());
        model.setDescription(dto.getDescription());
        model.setStartDate(dto.getStartDate());
        model.setEndDate(dto.getEndDate());
        model.setTargetEndDate(dto.getTargetEndDate());
        model.setDueDate(dto.getDueDate());
        model.setStatus(dto.getStatus());
        model.setType(dto.getType());
        model.setProgressPercentage(dto.getProgressPercentage());
        model.setBudgetRange(dto.getBudgetRange());
        model.setExpectedTeamSize(dto.getExpectedTeamSize());
        model.setArchived(dto.getArchived());

//        if (dto.getClientId() != null) {
//            model.setClient(userRepository.findById(dto.getClientId())
//                    .orElseThrow(() -> new HltCustomerException(ErrorCode.BUSINESS_NOT_FOUND)));
//        }

//        if (dto.getProjectManagerId() != null) {
//            model.setProjectManager(userRepository.findById(dto.getProjectManagerId())
//                    .orElseThrow(() -> new HltCustomerException(ErrorCode.BUSINESS_NOT_FOUND)));
//        }

        if (dto.getOwnerOrganizationId() != null) {
            OrganizationModel ownerOrg = new OrganizationModel();
            ownerOrg.setId(dto.getOwnerOrganizationId());
            model.setOwnerOrganization(ownerOrg);
        }

//        if (dto.getClientOrganizationId() != null) {
//            OrganizationModel clientOrg = new OrganizationModel();
//            clientOrg.setId(dto.getClientOrganizationId());
//            model.setClientOrganization(clientOrg);
//        }

        return model;
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

}
