package com.circuitguard.ai.usermanagement.services.impl;

import com.circuitguard.ai.usermanagement.dto.ProjectDTO;
import com.circuitguard.ai.usermanagement.dto.UserGroupDTO;
import com.circuitguard.ai.usermanagement.model.ProjectModel;
import com.circuitguard.ai.usermanagement.model.UserGroupModel;
import com.circuitguard.ai.usermanagement.populator.UserGroupPopulator;
import com.circuitguard.ai.usermanagement.repository.ProjectRepository;
import com.circuitguard.ai.usermanagement.repository.UserAssignmentRepository;
import com.circuitguard.ai.usermanagement.repository.UserGroupRepository;
import com.circuitguard.ai.usermanagement.services.UserGroupService;
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

    @Override
    @Transactional
    public UserGroupDTO create(UserGroupDTO dto) {
        validateDuplicateGroup(dto.getGroupName());

        ProjectModel project = fetchProject(dto.getProject());

        UserGroupModel model = UserGroupModel.builder()
                .groupName(dto.getGroupName())
                .description(dto.getDescription())
                .project(project)
                .build();

        userGroupRepository.save(model);
        return toDTO(model);
    }

    @Override
    @Transactional
    public UserGroupDTO update(Long id, UserGroupDTO dto) {
        UserGroupModel model = findGroupById(id);

        model.setGroupName(dto.getGroupName());
        model.setDescription(dto.getDescription());

        if (dto.getProject() != null && dto.getProject().getId() != null) {
            ProjectModel project = fetchProject(dto.getProject());
            model.setProject(project);
        }

        userGroupRepository.save(model);
        return toDTO(model);
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
                .map(this::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public UserGroupDTO getById(Long id) {
        return toDTO(findGroupById(id));
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

    private UserGroupDTO toDTO(UserGroupModel model) {
        UserGroupDTO dto = new UserGroupDTO();
        userGroupPopulator.populate(model, dto);
        return dto;
    }
}
