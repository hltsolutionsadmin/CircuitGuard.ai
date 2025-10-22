package com.circuitguard.ai.usermanagement.services.impl;

import com.circuitguard.ai.usermanagement.dto.UserAssignmentDTO;
import com.circuitguard.ai.usermanagement.dto.enums.AssignmentTargetType;
import com.circuitguard.ai.usermanagement.model.UserAssignmentModel;
import com.circuitguard.ai.usermanagement.model.UserModel;
import com.circuitguard.ai.usermanagement.populator.UserAssignmentPopulator;
import com.circuitguard.ai.usermanagement.repository.UserAssignmentRepository;
import com.circuitguard.ai.usermanagement.services.UserAssignmentService;
import com.skillrat.auth.exception.handling.ErrorCode;
import com.skillrat.auth.exception.handling.HltCustomerException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class UserAssignmentServiceImpl implements UserAssignmentService {

    private final UserAssignmentRepository userAssignmentRepository;
    private final UserAssignmentPopulator userAssignmentPopulator;

    @Autowired
    public UserAssignmentServiceImpl(UserAssignmentRepository userAssignmentRepository,
                                     UserAssignmentPopulator userAssignmentPopulator) {
        this.userAssignmentRepository = userAssignmentRepository;
        this.userAssignmentPopulator = userAssignmentPopulator;
    }

    @Override
    public UserAssignmentDTO assignUser(UserAssignmentDTO dto) {
        if (dto.getUserId() == null || dto.getTargetType() == null || dto.getTargetId() == null) {
            throw new HltCustomerException(ErrorCode.BUSINESS_VALIDATION_FAILED,
                    "User ID, TargetType, and TargetID are required");
        }

        UserAssignmentModel assignment;

        if (dto.getId() != null) {
            assignment = userAssignmentRepository.findById(dto.getId())
                    .orElseThrow(() -> new HltCustomerException(ErrorCode.BUSINESS_NOT_FOUND, "Assignment not found"));
        } else {
            assignment = new UserAssignmentModel();
        }

        UserModel user = new UserModel();
        user.setId(dto.getUserId());
        assignment.setUser(user);
        assignment.setTargetType(dto.getTargetType());
        assignment.setTargetId(dto.getTargetId());
        assignment.setRole(dto.getRole());
        assignment.setActive(dto.getActive() != null ? dto.getActive() : true);

        assignment = userAssignmentRepository.save(assignment);

        UserAssignmentDTO responseDTO = new UserAssignmentDTO();
        userAssignmentPopulator.populate(assignment, responseDTO);
        return responseDTO;
    }

    @Override
    public void removeAssignment(Long assignmentId) {
        UserAssignmentModel assignment = userAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new HltCustomerException(ErrorCode.BUSINESS_NOT_FOUND, "Assignment not found"));
        assignment.setActive(false);
        userAssignmentRepository.save(assignment);
    }

    @Override
    public UserAssignmentDTO getAssignmentById(Long assignmentId) {
        UserAssignmentModel assignment = userAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new HltCustomerException(ErrorCode.BUSINESS_NOT_FOUND, "Assignment not found"));

        UserAssignmentDTO dto = new UserAssignmentDTO();
        userAssignmentPopulator.populate(assignment, dto);
        return dto;
    }

    @Override
    public Page<UserAssignmentDTO> getAssignmentsByTarget(AssignmentTargetType targetType, Long targetId, Pageable pageable) {
        Page<UserAssignmentModel> page = userAssignmentRepository
                .findByTargetTypeAndTargetIdAndActiveTrue(targetType, targetId, pageable);

        return page.map(model -> {
            UserAssignmentDTO dto = new UserAssignmentDTO();
            userAssignmentPopulator.populate(model, dto);
            return dto;
        });
    }

    @Override
    public Page<UserAssignmentDTO> getAssignmentsByUser(Long userId, Pageable pageable) {
        Page<UserAssignmentModel> page = userAssignmentRepository
                .findByUser_IdAndActiveTrue(userId, pageable);

        return page.map(model -> {
            UserAssignmentDTO dto = new UserAssignmentDTO();
            userAssignmentPopulator.populate(model, dto);
            return dto;
        });
    }

    @Override
    public Page<UserAssignmentDTO> getAllAssignments(Pageable pageable) {
        Page<UserAssignmentModel> page = userAssignmentRepository.findAll(pageable);
        return page.map(model -> {
            UserAssignmentDTO dto = new UserAssignmentDTO();
            userAssignmentPopulator.populate(model, dto);
            return dto;
        });
    }


    @Override
    public Page<UserAssignmentDTO> getCurrentUserAssignments(HttpServletRequest request, Pageable pageable) {
        Long currentUserId = getCurrentUserId(request);
        if (currentUserId == null) {
            throw new HltCustomerException(ErrorCode.UNAUTHORIZED, "User not authenticated");
        }

        log.info("Fetching assignments for current user: {}", currentUserId);
        return getAssignmentsByUser(currentUserId, pageable);
    }

    @Override
    public Long getCurrentUserId(HttpServletRequest request) {
        // Example: Extract from custom header
        String userIdHeader = request.getHeader("X-User-Id");
        if (userIdHeader != null) {
            try {
                return Long.parseLong(userIdHeader);
            } catch (NumberFormatException e) {
                log.error("Invalid user ID format in header: {}", userIdHeader);
            }
        }


        return null;
    }
}
