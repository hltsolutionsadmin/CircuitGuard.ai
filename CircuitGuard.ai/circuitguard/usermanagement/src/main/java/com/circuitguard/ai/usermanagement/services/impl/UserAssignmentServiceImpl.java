package com.circuitguard.ai.usermanagement.services.impl;

import com.circuitguard.ai.usermanagement.dto.UserAssignmentDTO;
import com.circuitguard.ai.usermanagement.dto.enums.AssignmentTargetType;
import com.circuitguard.ai.usermanagement.model.RoleModel;
import com.circuitguard.ai.usermanagement.model.UserAssignmentModel;
import com.circuitguard.ai.usermanagement.model.UserModel;
import com.circuitguard.ai.usermanagement.populator.UserAssignmentPopulator;
import com.circuitguard.ai.usermanagement.repository.UserAssignmentRepository;
import com.circuitguard.ai.usermanagement.services.UserAssignmentService;
import com.circuitguard.auth.exception.handling.ErrorCode;
import com.circuitguard.auth.exception.handling.HltCustomerException;
import com.circuitguard.commonservice.enums.ERole;
import lombok.AllArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;

@Service
@Transactional
@AllArgsConstructor
public class UserAssignmentServiceImpl implements UserAssignmentService {

    private final UserAssignmentRepository userAssignmentRepository;
    private final UserAssignmentPopulator userAssignmentPopulator;
    private final RoleServiceImpl roleService;
    private final UserServiceImpl userService;

    @Override
    public UserAssignmentDTO assignUser(UserAssignmentDTO dto) {
        if (dto.getTargetType() == null || dto.getTargetId() == null) {
            throw new HltCustomerException(ErrorCode.INVALID_ASSIGNMENT_REQUEST);
        }
        if (dto.getUserId() == null){
            UserModel userModel = registerNewAdmin(dto);
            dto.setUserId(userModel.getId());

        }


        UserAssignmentModel assignment = (dto.getId() != null)
                ? userAssignmentRepository.findById(dto.getId())
                .orElseThrow(() -> new HltCustomerException(ErrorCode.ASSIGNMENT_NOT_FOUND))
                : new UserAssignmentModel();

        assignment.setUser(new UserModel());
        assignment.getUser().setId(dto.getUserId());
        assignment.setTargetType(dto.getTargetType());
        assignment.setTargetId(dto.getTargetId());
        assignment.setRole(dto.getRole());
        assignment.setActive(dto.getActive() != null ? dto.getActive() : true);

        assignment = userAssignmentRepository.save(assignment);

        UserAssignmentDTO responseDTO = new UserAssignmentDTO();
        userAssignmentPopulator.populate(assignment, responseDTO);
        return responseDTO;
    }

    private UserModel registerNewAdmin(UserAssignmentDTO dto) {
        UserModel user = new UserModel();
        user.setUsername(dto.getUsername());
        user.setFullName(dto.getFullName());
        user.setPrimaryContact(dto.getPrimaryContact());
        user.setPrimaryContactHash(DigestUtils.sha256Hex(dto.getPrimaryContact()));
        user.setPassword(dto.getPassword());
        user.setRecentActivityDate(LocalDate.now());

        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            user.setEmail(dto.getEmail());
            user.setEmailHash(DigestUtils.sha256Hex(dto.getEmail().trim().toLowerCase()));
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
        Page<UserAssignmentModel> page = userAssignmentRepository.findByTargetTypeAndTargetIdAndActiveTrue(targetType, targetId, pageable);
        return page.map(assignment -> {
            UserAssignmentDTO dto = new UserAssignmentDTO();
            userAssignmentPopulator.populate(assignment, dto);
            return dto;
        });
    }

    @Override
    public Page<UserAssignmentDTO> getAssignmentsByUser(Long userId, Pageable pageable) {
        Page<UserAssignmentModel> page = userAssignmentRepository.findByUser_IdAndActiveTrue(userId, pageable);
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
