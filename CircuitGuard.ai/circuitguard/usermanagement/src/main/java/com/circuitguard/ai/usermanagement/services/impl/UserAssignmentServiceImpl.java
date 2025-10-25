package com.circuitguard.ai.usermanagement.services.impl;

import com.circuitguard.ai.usermanagement.dto.UserAssignmentDTO;
import com.circuitguard.ai.usermanagement.dto.enums.AssignmentTargetType;
import com.circuitguard.ai.usermanagement.model.RoleModel;
import com.circuitguard.ai.usermanagement.model.UserAssignmentModel;
import com.circuitguard.ai.usermanagement.model.UserGroupModel;
import com.circuitguard.ai.usermanagement.model.UserModel;
import com.circuitguard.ai.usermanagement.populator.UserAssignmentPopulator;
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
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@AllArgsConstructor
public class UserAssignmentServiceImpl implements UserAssignmentService {

    private final UserAssignmentRepository userAssignmentRepository;
    private final UserGroupRepository userGroupRepository;
    private final UserAssignmentPopulator userAssignmentPopulator;
    private final RoleServiceImpl roleService;
    private final UserServiceImpl userService;

    @Override
    public UserAssignmentDTO assignUserToTarget(UserAssignmentDTO dto) {
        if (dto.getTargetType() == null || dto.getTargetId() == null) {
            throw new HltCustomerException(ErrorCode.INVALID_ASSIGNMENT_REQUEST);
        }
        if (dto.getTargetType() == AssignmentTargetType.ORGANIZATION) {
            return handleOrganizationAssignment(dto);
        } else {
            return handleTargetAssignment(dto);
        }
    }

    private UserAssignmentDTO handleOrganizationAssignment(UserAssignmentDTO dto) {

        // Register new user
        UserModel user = registerNewAdmin(dto);

        // Create assignment
        UserAssignmentModel assignment = new UserAssignmentModel();
        assignment.setUser(user);
        assignment.setTargetType(AssignmentTargetType.ORGANIZATION);
        assignment.setTargetId(dto.getTargetId());
        assignment.setRole(dto.getRole());
        assignment.setActive(dto.getActive() != null ? dto.getActive() : true);

        assignment = userAssignmentRepository.save(assignment);

        UserAssignmentDTO responseDTO = new UserAssignmentDTO();
        userAssignmentPopulator.populate(assignment, responseDTO);
        return responseDTO;
    }

    private UserAssignmentDTO handleTargetAssignment(UserAssignmentDTO dto) {
        if (dto.getUserId() == null) {
            throw new HltCustomerException(ErrorCode.USER_NOT_FOUND);
        }

        // Check if user is already assigned to this target
        Optional<UserAssignmentModel> existingAssignment = userAssignmentRepository
                .findByUser_IdAndTargetTypeAndTargetId(dto.getUserId(), dto.getTargetType(), dto.getTargetId());

        if (existingAssignment.isPresent()) {
            throw new HltCustomerException(ErrorCode.USER_ALREADY_REGISTERED);
        }

        // Fetch user reference
        UserModel user = userService.findById(dto.getUserId());
        if (user == null) {
            throw new HltCustomerException(ErrorCode.USER_NOT_FOUND);
        }
        UserAssignmentModel assignment = new UserAssignmentModel();
        assignment.setUser(user);
        assignment.setTargetType(dto.getTargetType());
        assignment.setTargetId(dto.getTargetId());
        assignment.setRole(dto.getRole());
        assignment.setActive(dto.getActive() != null ? dto.getActive() : true);

        // Attach groups if any
        if (dto.getGroupIds() != null && !dto.getGroupIds().isEmpty()) {
            Set<UserGroupModel> groups = new HashSet<>(userGroupRepository.findAllById(dto.getGroupIds()));
            assignment.setGroups(groups);
        }

        // Save
        assignment = userAssignmentRepository.save(assignment);

        // Populate DTO
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

        return userService.saveUser(user);
    }

    @Override
    public void removeAssignment(Long assignmentId) {
        UserAssignmentModel assignment = userAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new HltCustomerException(ErrorCode.ASSIGNMENT_NOT_FOUND));
        assignment.setActive(false);
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
}
