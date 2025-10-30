package com.circuitguard.ai.usermanagement.services.impl;

import com.circuitguard.ai.usermanagement.dto.UserAssignmentDTO;
import com.circuitguard.ai.usermanagement.dto.UserDTO;
import com.circuitguard.ai.usermanagement.dto.enums.AssignmentRole;
import com.circuitguard.ai.usermanagement.dto.enums.AssignmentTargetType;
import com.circuitguard.ai.usermanagement.model.*;
import com.circuitguard.ai.usermanagement.populator.UserAssignmentPopulator;
import com.circuitguard.ai.usermanagement.populator.UserPopulator;
import com.circuitguard.ai.usermanagement.repository.OrganizationRepository;
import com.circuitguard.ai.usermanagement.repository.ProjectRepository;
import com.circuitguard.ai.usermanagement.repository.UserAssignmentRepository;
import com.circuitguard.ai.usermanagement.repository.UserGroupRepository;
import com.circuitguard.ai.usermanagement.services.UserAssignmentService;
import com.circuitguard.auth.exception.handling.ErrorCode;
import com.circuitguard.auth.exception.handling.HltCustomerException;
import com.circuitguard.commonservice.enums.ERole;
import lombok.AllArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class UserAssignmentServiceImpl implements UserAssignmentService {

    private final UserAssignmentRepository userAssignmentRepository;
    private final UserGroupRepository userGroupRepository;
    private final UserAssignmentPopulator userAssignmentPopulator;
    private final OrganizationRepository organizationRepository;
    private final ProjectRepository projectRepository;
    private final RoleServiceImpl roleService;
    private final UserServiceImpl userService;
    private final UserPopulator userPopulator;

    @Override
    @Transactional
    public List<UserAssignmentDTO> assignUserToTarget(UserAssignmentDTO dto) {
        validateAssignmentRequest(dto);

        return (dto.getTargetType() == AssignmentTargetType.ORGANIZATION)
                ? List.of(handleOrganizationAssignment(dto))
                : handleTargetAssignment(dto);
    }

    private UserAssignmentDTO handleOrganizationAssignment(UserAssignmentDTO dto) {
        UserModel user = registerNewAdmin(dto);

        OrganizationModel organization = organizationRepository.findByIdAndActiveTrue(dto.getTargetId())
                .orElseThrow(() -> new HltCustomerException(ErrorCode.ORGANIZATION_NOT_FOUND));

        UserAssignmentModel assignment = buildAssignment(user, dto, organization.getId(), AssignmentTargetType.ORGANIZATION);

        if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
            assignment.setRoles(new HashSet<>(dto.getRoles()));
        }

        assignment = userAssignmentRepository.save(assignment);
        return convertToDTO(assignment);
    }

    @Transactional
    private List<UserAssignmentDTO> handleTargetAssignment(UserAssignmentDTO dto) {
        ProjectModel project = projectRepository.findById(dto.getTargetId())
                .orElseThrow(() -> new HltCustomerException(ErrorCode.PROJECT_NOT_FOUND));

        List<UserAssignmentDTO> responseList = new ArrayList<>();

        if (dto.getUserIds() == null || dto.getUserIds().isEmpty()) {
            throw new HltCustomerException(ErrorCode.USER_REQUIRED);
        }

        if (dto.getRoles() == null || dto.getRoles().isEmpty()) {
            throw new HltCustomerException(ErrorCode.ROLES_REQUIRED);
        }

        if (dto.getUserIds().size() != dto.getRoles().size()) {
            throw new HltCustomerException(ErrorCode.USER_ROLE_MISMATCH);
        }

        for (int i = 0; i < dto.getUserIds().size(); i++) {
            Long userId = dto.getUserIds().get(i);
            AssignmentRole role = dto.getRoles().get(i);

            UserModel user = Optional.ofNullable(userService.findById(userId))
                    .orElseThrow(() -> new HltCustomerException(ErrorCode.USER_NOT_FOUND));

            userAssignmentRepository.findByUser_IdAndTargetTypeAndTargetId(userId, dto.getTargetType(), dto.getTargetId())
                    .ifPresent(existing -> {
                        throw new HltCustomerException(ErrorCode.USER_ALREADY_REGISTERED);
                    });

            UserAssignmentModel assignment = buildAssignment(user, dto, project.getId(), dto.getTargetType());
            assignment.setRoles(Set.of(role));

            if (dto.getGroupIds() != null && !dto.getGroupIds().isEmpty()) {
                Set<UserGroupModel> groups = validateAndFetchGroups(dto.getGroupIds());
                assignment.setGroups(groups);
            }

            UserAssignmentModel saved = userAssignmentRepository.save(assignment);

            UserAssignmentDTO responseDTO = new UserAssignmentDTO();
            userAssignmentPopulator.populate(saved, responseDTO);
            responseList.add(responseDTO);
        }

        return responseList;
    }


    private void validateAssignmentRequest(UserAssignmentDTO dto) {
        if (dto.getTargetType() == null || dto.getTargetId() == null) {
            throw new HltCustomerException(ErrorCode.INVALID_ASSIGNMENT_REQUEST);
        }
        if (dto.getTargetType() != AssignmentTargetType.ORGANIZATION) {
            if (dto.getUserIds() == null || dto.getUserIds().isEmpty()) {
                throw new HltCustomerException(ErrorCode.USER_IDS_REQUIRED);
            }
        }
    }

    private Set<UserGroupModel> validateAndFetchGroups(List<Long> groupIds) {
        if (groupIds == null || groupIds.isEmpty()) {
            return Collections.emptySet();
        }

        Set<Long> inputIds = new HashSet<>(groupIds);
        List<UserGroupModel> groups = userGroupRepository.findAllById(inputIds);

        if (groups.size() != inputIds.size()) {
            Set<Long> foundIds = groups.stream().map(UserGroupModel::getId).collect(Collectors.toSet());

            Set<Long> missingIds = inputIds.stream().filter(id -> !foundIds.contains(id)).collect(Collectors.toSet());

            throw new HltCustomerException(ErrorCode.GROUP_NOT_FOUND, "Group(s) not found for ID(s): " + missingIds);
        }

        return new HashSet<>(groups);
    }


    private UserAssignmentModel buildAssignment(UserModel user, UserAssignmentDTO dto, Long targetId, AssignmentTargetType type) {
        UserAssignmentModel assignment = new UserAssignmentModel();
        assignment.setUser(user);
        assignment.setTargetType(type);
        assignment.setTargetId(targetId);
        assignment.setActive(Optional.ofNullable(dto.getActive()).orElse(true));

        if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
            assignment.setRoles(new HashSet<>(dto.getRoles()));
        }

        return assignment;
    }


    private UserAssignmentDTO convertToDTO(UserAssignmentModel assignment) {
        UserAssignmentDTO responseDTO = new UserAssignmentDTO();
        userAssignmentPopulator.populate(assignment, responseDTO);
        return responseDTO;
    }


    private UserModel registerNewAdmin(UserAssignmentDTO userAssignmentDTO) {
        UserModel user = new UserModel();
        user.setUsername(userAssignmentDTO.getUsername());
        user.setFullName(userAssignmentDTO.getFullName());
        user.setPrimaryContact(userAssignmentDTO.getPrimaryContact());
        user.setPrimaryContactHash(DigestUtils.sha256Hex(userAssignmentDTO.getPrimaryContact()));
        user.setPassword(userAssignmentDTO.getPassword());
        user.setRecentActivityDate(LocalDate.now());

        if (userAssignmentDTO.getEmail() != null && !userAssignmentDTO.getEmail().isBlank()) {
            user.setEmail(userAssignmentDTO.getEmail());
            user.setEmailHash(DigestUtils.sha256Hex(userAssignmentDTO.getEmail().trim().toLowerCase()));
        }

        Set<RoleModel> roles = Set.of(
                roleService.findByErole(ERole.ROLE_USER)
        );
        user.setRoles(roles);

        try {
            return userService.saveUser(user);
        } catch (Exception e) {
            throw new HltCustomerException(ErrorCode.USER_CREATION_FAILED);
        }
    }

    @Override
    public void removeAssignment(Long assignmentId) {
        UserAssignmentModel assignment = userAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new HltCustomerException(ErrorCode.ASSIGNMENT_NOT_FOUND));
        assignment.getGroups().clear();
        assignment.getRoles().clear();
        userAssignmentRepository.save(assignment);
    }

    @Override
    @Transactional
    public void updateAssignmentStatus(Long assignmentId, boolean active) {
        UserAssignmentModel assignment = userAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new HltCustomerException(ErrorCode.ASSIGNMENT_NOT_FOUND));

        if (Boolean.TRUE.equals(active) && Boolean.TRUE.equals(assignment.getActive())) {
            throw new HltCustomerException(ErrorCode.ASSIGNMENT_ALREADY_ACTIVE);
        }

        if (Boolean.FALSE.equals(active) && Boolean.FALSE.equals(assignment.getActive())) {
            throw new HltCustomerException(ErrorCode.ASSIGNMENT_ALREADY_INACTIVE);
        }

        assignment.setActive(active);
        userAssignmentRepository.save(assignment);
    }



    @Override
    public UserAssignmentDTO getAssignmentById(Long assignmentId) {
        UserAssignmentModel assignment = userAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new HltCustomerException(ErrorCode.ASSIGNMENT_NOT_FOUND));

        UserAssignmentDTO dto = new UserAssignmentDTO();
        userAssignmentPopulator.populate(assignment, dto);
        return dto;
    }

    @Override
    public Page<UserAssignmentDTO> getAssignmentsByTarget(AssignmentTargetType targetType, Long targetId, Pageable pageable) {
        Page<UserAssignmentModel> page = userAssignmentRepository.findByTargetIdAndTargetType(targetId, targetType, pageable);
        return page.map(assignment -> {
            UserAssignmentDTO dto = new UserAssignmentDTO();
            userAssignmentPopulator.populate(assignment, dto);
            return dto;
        });
    }

    @Override
    public Page<UserAssignmentDTO> getAssignmentsByUser(Long userId, Pageable pageable) {
        Page<UserAssignmentModel> page = userAssignmentRepository.findByUserId(userId, pageable);
        return page.map(assignment -> {
            UserAssignmentDTO dto = new UserAssignmentDTO();
            userAssignmentPopulator.populate(assignment, dto);
            return dto;
        });
    }

    @Override
    public Page<UserAssignmentDTO> getAllAssignments(Pageable pageable) {
        Page<UserAssignmentModel> page = userAssignmentRepository.findAll(pageable);
        return page.map(assignment -> {
            UserAssignmentDTO dto = new UserAssignmentDTO();
            userAssignmentPopulator.populate(assignment, dto);
            return dto;
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> getUsersByGroup(Long groupId, Pageable pageable) {
        Page<UserModel> userPage = userAssignmentRepository.findUsersByGroupId(groupId, pageable);

        if (userPage.isEmpty()) {
            throw new HltCustomerException(ErrorCode.BUSINESS_NOT_FOUND, "No users found for the given group");
        }

        return userPage.map(user -> userPopulator.toDTO(user));
    }


    @Override
    public Page<UserAssignmentDTO> getAssignmentsByTargetAndRoles(
            AssignmentTargetType targetType,
            Long targetId,
            Set<AssignmentRole> roles,
            Pageable pageable) {

        Page<UserAssignmentModel> page = userAssignmentRepository.findByTargetAndRoles(targetType, targetId, roles, pageable);

        return page.map(assignment -> {
            UserAssignmentDTO dto = new UserAssignmentDTO();
            userAssignmentPopulator.populate(assignment, dto);
            return dto;
        });
    }



}
