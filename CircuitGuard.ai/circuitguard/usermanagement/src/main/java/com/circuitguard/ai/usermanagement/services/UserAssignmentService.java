package com.circuitguard.ai.usermanagement.services;

import com.circuitguard.ai.usermanagement.dto.UserAssignmentDTO;
import com.circuitguard.ai.usermanagement.dto.enums.AssignmentTargetType;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserAssignmentService {

    UserAssignmentDTO assignUser(UserAssignmentDTO userAssignmentDTO);

    void removeAssignment(Long assignmentId);

    UserAssignmentDTO getAssignmentById(Long assignmentId);

    Page<UserAssignmentDTO> getAssignmentsByTarget(AssignmentTargetType targetType, Long targetId, Pageable pageable);

    Page<UserAssignmentDTO> getAssignmentsByUser(Long userId, Pageable pageable);

    Page<UserAssignmentDTO> getAllAssignments(Pageable pageable);

    Page<UserAssignmentDTO> getCurrentUserAssignments(HttpServletRequest request, Pageable pageable);

    Long getCurrentUserId(HttpServletRequest request);


}
