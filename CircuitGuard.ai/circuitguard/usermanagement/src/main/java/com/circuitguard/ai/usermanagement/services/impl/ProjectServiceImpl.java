package com.circuitguard.ai.usermanagement.services.impl;

import com.circuitguard.ai.usermanagement.dto.OrganizationDTO;
import com.circuitguard.ai.usermanagement.dto.ProjectDTO;
import com.circuitguard.ai.usermanagement.dto.ProjectStatsDTO;
import com.circuitguard.ai.usermanagement.dto.enums.ProjectStatus;
import com.circuitguard.ai.usermanagement.model.OrganizationModel;
import com.circuitguard.ai.usermanagement.model.ProjectModel;
import com.circuitguard.ai.usermanagement.model.RoleModel;
import com.circuitguard.ai.usermanagement.model.UserModel;
import com.circuitguard.ai.usermanagement.populator.ProjectPopulator;
import com.circuitguard.ai.usermanagement.repository.OrganizationRepository;
import com.circuitguard.ai.usermanagement.repository.ProjectRepository;
import com.circuitguard.ai.usermanagement.repository.UserRepository;
import com.circuitguard.ai.usermanagement.repository.UserAssignmentRepository;
import com.circuitguard.ai.usermanagement.services.ProjectService;
import com.circuitguard.ai.usermanagement.services.RoleService;
import com.circuitguard.ai.usermanagement.services.UserService;
import com.circuitguard.ai.usermanagement.utils.ProjectCodeGenerator;
import com.circuitguard.auth.exception.handling.ErrorCode;
import com.circuitguard.auth.exception.handling.HltCustomerException;
import com.circuitguard.utils.SecurityUtils;
import com.circuitguard.commonservice.enums.ERole;
import lombok.AllArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
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
    private final RoleService roleService;
    private final UserService userService;
    private final UserAssignmentRepository userAssignmentRepository;

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
    public Page<ProjectDTO> fetchProjectsWithFilters(Pageable pageable, Long projectId, Long clientId, Long managerId, String status) {
        Page<ProjectModel> page = fetchProjectsWithFilter(pageable, projectId, clientId, managerId, status);
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


    @Override
    public Page<ProjectDTO> getProjectsForCurrentUser(Pageable pageable) {
        Long userId = SecurityUtils.getCurrentUserDetails().getId();

        java.util.List<com.circuitguard.ai.usermanagement.model.UserAssignmentModel> assignments =
                userAssignmentRepository.findByUser_IdAndActiveTrue(userId);

        java.util.List<Long> projectIds = assignments.stream()
                .filter(a -> a.getTargetType() == com.circuitguard.ai.usermanagement.dto.enums.AssignmentTargetType.PROJECT)
                .map(com.circuitguard.ai.usermanagement.model.UserAssignmentModel::getTargetId)
                .distinct()
                .collect(java.util.stream.Collectors.toList());

        if (projectIds.isEmpty()) {
            return new PageImpl<>(java.util.Collections.emptyList(), pageable, 0);
        }

        Page<ProjectModel> page = projectRepository.findByIdIn(projectIds, pageable);
        java.util.List<ProjectDTO> dtos = page.getContent().stream()
                .map(projectPopulator::toDTO)
                .collect(java.util.stream.Collectors.toList());

        return new PageImpl<>(dtos, pageable, page.getTotalElements());
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
        if (dto.getSlaTier() != null) {
            model.setSlaTier(dto.getSlaTier());
        } else if (model.getId() == null) {
            model.setSlaTier(com.circuitguard.ai.usermanagement.dto.enums.SlaTier.STANDARD);
        }
        if (dto.getProgressPercentage() != null) model.setProgressPercentage(dto.getProgressPercentage());
        if (dto.getBudgetRange() != null) model.setBudgetRange(dto.getBudgetRange());
        if (dto.getExpectedTeamSize() != null) model.setExpectedTeamSize(dto.getExpectedTeamSize());
        if (dto.getArchived() != null) model.setArchived(dto.getArchived());

        if (dto.getClientId() != null) {
            model.setClient(getUserById(dto.getClientId(), ErrorCode.CLIENT_NOT_FOUND));
        } else if (dto.getClientUsername() != null && dto.getCleintFullName() != null) {
            UserModel newClient = registerNewClient(dto);
            model.setClient(newClient);
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
    private UserModel registerNewClient(ProjectDTO dto) {
        UserModel user = new UserModel();

        user.setUsername(dto.getClientEmail());
        user.setFullName(dto.getCleintFullName());
        user.setPrimaryContact(dto.getClientEmail());
        user.setPrimaryContactHash(DigestUtils.sha256Hex(dto.getClientEmail()));
        user.setPassword(dto.getCleintPassword());
        user.setRecentActivityDate(LocalDate.now());

        if (dto.getClientEmail() != null && !dto.getClientEmail().isBlank()) {
            user.setEmail(dto.getClientEmail().trim());
            user.setEmailHash(DigestUtils.sha256Hex(dto.getClientEmail().trim().toLowerCase()));
        }

        Set<RoleModel> roles = Set.of(
                roleService.findByErole(ERole.ROLE_CLIENT_ADMIN),
                roleService.findByErole(ERole.ROLE_USER)
        );
        user.setRoles(roles);

        return userService.saveUser(user);
    }


    private OrganizationModel getOrganizationById(Long orgId, ErrorCode errorCode) {
        if (orgId == null) return null;
        return organizationRepository.findById(orgId)
                .orElseThrow(() -> new HltCustomerException(errorCode));
    }


    private Page<ProjectModel> fetchProjectsWithFilter(Pageable pageable, Long organisationId, Long clientId, Long managerId, String statusStr) {
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
            return projectRepository.findByOwnerOrganization_IdAndStatus(organisationId,status, pageable);
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
