package com.circuitguard.ai.usermanagement.controllers;

import com.circuitguard.ai.usermanagement.dto.UserAssignmentDTO;
import com.circuitguard.ai.usermanagement.dto.UserDTO;
import com.circuitguard.ai.usermanagement.dto.enums.AssignmentTargetType;
import com.circuitguard.ai.usermanagement.services.UserAssignmentService;
import com.circuitguard.commonservice.dto.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
public class UserAssignmentController {

    private final UserAssignmentService userAssignmentService;

    @Autowired
    public UserAssignmentController(UserAssignmentService userAssignmentService) {
        this.userAssignmentService = userAssignmentService;
    }


    @PostMapping
    public ResponseEntity<StandardResponse<UserAssignmentDTO>> assignUser(@RequestBody UserAssignmentDTO dto) {
        UserAssignmentDTO saved = userAssignmentService.assignUserToTarget(dto);
        return ResponseEntity.ok(StandardResponse.single("User assignment saved successfully", saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StandardResponse<UserAssignmentDTO>> getAssignmentById(@PathVariable Long id) {
        UserAssignmentDTO dto = userAssignmentService.getAssignmentById(id);
        return ResponseEntity.ok(StandardResponse.single("User assignment fetched successfully", dto));
    }


    @GetMapping("/target")
    public ResponseEntity<StandardResponse<Page<UserAssignmentDTO>>> getAssignmentsByTarget(
            @RequestParam AssignmentTargetType targetType,
            @RequestParam Long targetId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<UserAssignmentDTO> result = userAssignmentService.getAssignmentsByTarget(targetType, targetId, pageable);
        return ResponseEntity.ok(StandardResponse.page("Assignments fetched successfully", result));
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<StandardResponse<Page<UserAssignmentDTO>>> getAssignmentsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<UserAssignmentDTO> result = userAssignmentService.getAssignmentsByUser(userId, pageable);
        return ResponseEntity.ok(StandardResponse.page("Assignments fetched successfully", result));
    }


    @GetMapping
    public ResponseEntity<StandardResponse<Page<UserAssignmentDTO>>> getAllAssignments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<UserAssignmentDTO> result = userAssignmentService.getAllAssignments(pageable);
        return ResponseEntity.ok(StandardResponse.page("All assignments fetched successfully", result));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<StandardResponse<Void>> removeAssignment(@PathVariable Long id) {
        userAssignmentService.removeAssignment(id);
        return ResponseEntity.ok(StandardResponse.message("User assignment removed successfully"));
    }

    @GetMapping("/{groupId}/users")
    public ResponseEntity<StandardResponse<Page<UserDTO>>> getUsersByGroup(
            @PathVariable Long groupId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<UserDTO> result = userAssignmentService.getUsersByGroup(groupId, pageable);
        return ResponseEntity.ok(StandardResponse.page("Users fetched successfully", result));
    }

}
