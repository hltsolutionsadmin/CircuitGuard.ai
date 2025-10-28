package com.circuitguard.ai.usermanagement.services.impl;

import com.circuitguard.ai.usermanagement.dto.ProjectDTO;
import com.circuitguard.ai.usermanagement.dto.UserGroupDTO;
import com.circuitguard.ai.usermanagement.model.ProjectModel;
import com.circuitguard.ai.usermanagement.model.UserGroupModel;
import com.circuitguard.ai.usermanagement.model.UserModel;
import com.circuitguard.ai.usermanagement.populator.UserGroupPopulator;
import com.circuitguard.ai.usermanagement.repository.ProjectRepository;
import com.circuitguard.ai.usermanagement.repository.UserAssignmentRepository;
import com.circuitguard.ai.usermanagement.repository.UserGroupRepository;
import com.circuitguard.ai.usermanagement.services.UserGroupService;
import com.circuitguard.ai.usermanagement.services.UserService;
import com.circuitguard.auth.exception.handling.ErrorCode;
import com.circuitguard.auth.exception.handling.HltCustomerException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserGroupServiceImpl implements UserGroupService {

    private final UserGroupRepository userGroupRepository;
    private final ProjectRepository projectRepository;
    private final UserGroupPopulator userGroupPopulator;
    private final UserAssignmentRepository userAssignmentRepository;
    private final UserService userService;


    @Override
    @Transactional
    public UserGroupDTO create(UserGroupDTO dto) {
        validateDuplicateGroup(dto.getGroupName());

        ProjectModel project = fetchProject(dto.getProject());
        UserModel groupLead = resolveGroupLead(dto);

        UserGroupModel model = UserGroupModel.builder()
                .groupName(dto.getGroupName())
                .description(dto.getDescription())
                .project(project)
                .groupLead(groupLead)
                .build();

        userGroupRepository.save(model);
        return userGroupPopulator.toDTO(model);
    }


    @Override
    @Transactional
    public UserGroupDTO update(Long id, UserGroupDTO dto) {
        UserGroupModel model = findGroupById(id);

        if (dto.getGroupName() != null && !dto.getGroupName().isBlank()) {
            model.setGroupName(dto.getGroupName());
        }

        if (dto.getDescription() != null) {
            model.setDescription(dto.getDescription());
        }

        if (dto.getProject() != null && dto.getProject().getId() != null) {
            ProjectModel project = fetchProject(dto.getProject());
            model.setProject(project);
        }

        if (dto.getGroupLead() != null && dto.getGroupLead().getId() != null) {
            UserModel groupLead = resolveGroupLead(dto);
            model.setGroupLead(groupLead);
        }

        userGroupRepository.save(model);
        return userGroupPopulator.toDTO(model);
    }


    @Override
    @Transactional
    public void delete(Long id) {
        UserGroupModel model = findGroupById(id);
        userAssignmentRepository.deleteByUserGroupId(id);
        userGroupRepository.delete(model);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<UserGroupDTO> getAll(Pageable pageable) {
        return userGroupRepository.findAll(pageable)
                .map(userGroupPopulator::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public UserGroupDTO getById(Long id) {
        return userGroupPopulator.toDTO(findGroupById(id));
    }

    @Transactional(readOnly = true)
    public Page<UserGroupDTO> getGroupsByProjectId(Long projectId, Pageable pageable) {
        ProjectModel project = projectRepository.findById(projectId)
                .orElseThrow(() -> new HltCustomerException(ErrorCode.PROJECT_NOT_FOUND));

        return userGroupRepository.findByProjectId(project.getId(), pageable)
                .map(userGroupPopulator::toDTO);
    }


    private void validateDuplicateGroup(String groupName) {
        if (userGroupRepository.existsByGroupNameIgnoreCase(groupName)) {
            throw new HltCustomerException(ErrorCode.DUPLICATE_GROUP_NAME);
        }
    }

    private ProjectModel fetchProject(ProjectDTO projectDTO) {
        if (projectDTO == null || projectDTO.getId() == null) {
            throw new HltCustomerException(ErrorCode.INVALID_PROJECT_REFERENCE);
        }

        return projectRepository.findById(projectDTO.getId())
                .orElseThrow(() -> new HltCustomerException(ErrorCode.PROJECT_NOT_FOUND));
    }

    private UserGroupModel findGroupById(Long id) {
        return userGroupRepository.findById(id)
                .orElseThrow(() -> new HltCustomerException(ErrorCode.GROUP_NOT_FOUND));
    }

    private UserModel resolveGroupLead(UserGroupDTO dto) {
        if (dto.getGroupLead() == null || dto.getGroupLead().getId() == null) {
            return null;
        }

        UserModel groupLead = userService.findById(dto.getGroupLead().getId());
        if (groupLead == null) {
            throw new HltCustomerException(ErrorCode.USER_NOT_FOUND);
        }
        return groupLead;
    }
}
