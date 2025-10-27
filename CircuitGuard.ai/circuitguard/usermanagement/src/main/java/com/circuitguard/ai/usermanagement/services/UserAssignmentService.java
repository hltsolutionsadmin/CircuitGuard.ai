package com.circuitguard.ai.usermanagement.services;

import com.circuitguard.ai.usermanagement.dto.UserAssignmentDTO;
import com.circuitguard.ai.usermanagement.dto.UserDTO;
import com.circuitguard.ai.usermanagement.dto.enums.AssignmentTargetType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserAssignmentService {


    List<UserAssignmentDTO> assignUserToTarget(UserAssignmentDTO userAssignmentDTO);

    void removeAssignment(Long assignmentId);

    UserAssignmentDTO getAssignmentById(Long assignmentId);

    Page<UserAssignmentDTO> getAssignmentsByTarget(AssignmentTargetType targetType, Long targetId, Pageable pageable);

    Page<UserAssignmentDTO> getAssignmentsByUser(Long userId, Pageable pageable);

    Page<UserAssignmentDTO> getAllAssignments(Pageable pageable);

    Page<UserDTO> getUsersByGroup(Long groupId, Pageable pageable);

}
