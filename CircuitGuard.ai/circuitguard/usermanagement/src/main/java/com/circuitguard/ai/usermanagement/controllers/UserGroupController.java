package com.circuitguard.ai.usermanagement.controllers;

import com.circuitguard.ai.usermanagement.dto.UserGroupDTO;
import com.circuitguard.ai.usermanagement.services.UserGroupService;
import com.circuitguard.commonservice.dto.StandardResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usergroups")
@RequiredArgsConstructor
public class UserGroupController {

    private final UserGroupService userGroupService;

    @PostMapping
    public StandardResponse<UserGroupDTO> create(@Valid @RequestBody UserGroupDTO dto) {
        UserGroupDTO createdGroup = userGroupService.create(dto);
        return StandardResponse.single("User group created successfully", createdGroup);
    }


    @PutMapping("/{id}")
    public StandardResponse<UserGroupDTO> update(@PathVariable Long id, @Valid @RequestBody UserGroupDTO dto) {
        UserGroupDTO updatedGroup = userGroupService.update(id, dto);
        return StandardResponse.single("User group updated successfully", updatedGroup);
    }

    @DeleteMapping("/{id}")
    public StandardResponse<String> delete(@PathVariable Long id) {
        userGroupService.delete(id);
        return StandardResponse.message("User group deleted successfully");
    }


    @GetMapping
    public StandardResponse<Page<UserGroupDTO>> getAll(Pageable pageable) {
        Page<UserGroupDTO> groups = userGroupService.getAll(pageable);
        return StandardResponse.page("User groups fetched successfully", groups);
    }

    @GetMapping("/project/{projectId}")
    public StandardResponse<Page<UserGroupDTO>> getGroupsByProjectId(
            @PathVariable Long projectId,
            Pageable pageable) {

        Page<UserGroupDTO> groups = userGroupService.getGroupsByProjectId(projectId, pageable);
        return StandardResponse.page("User groups fetched successfully", groups);
    }

    @GetMapping("/{id}")
    public StandardResponse<UserGroupDTO> getById(@PathVariable Long id) {
        UserGroupDTO group = userGroupService.getById(id);
        return StandardResponse.single("User group fetched successfully", group);
    }
}
