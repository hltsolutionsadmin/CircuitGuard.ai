package com.circuitguard.ai.usermanagement.services.impl;

import com.circuitguard.ai.usermanagement.dto.UserAssignmentDTO;
import com.circuitguard.ai.usermanagement.dto.enums.AssignmentTargetType;
import com.circuitguard.ai.usermanagement.model.UserAssignmentModel;
import com.circuitguard.ai.usermanagement.populator.UserAssignmentPopulator;
import com.circuitguard.ai.usermanagement.repository.UserAssignmentRepository;
import com.circuitguard.ai.usermanagement.services.UserAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
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
            throw new IllegalArgumentException("User ID, TargetType, and TargetID are required");
        }

        UserAssignmentModel assignment;

        if (dto.getId() != null) {
            assignment = userAssignmentRepository.findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException("Assignment not found"));
        } else {
            assignment = new UserAssignmentModel();
        }

        assignment.setUser(new com.circuitguard.ai.usermanagement.model.UserModel());
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

    @Override
    public void removeAssignment(Long assignmentId) {
        UserAssignmentModel assignment = userAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        assignment.setActive(false);
        userAssignmentRepository.save(assignment);
    }

    @Override
    public UserAssignmentDTO getAssignmentById(Long assignmentId) {
        UserAssignmentModel assignment = userAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

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
