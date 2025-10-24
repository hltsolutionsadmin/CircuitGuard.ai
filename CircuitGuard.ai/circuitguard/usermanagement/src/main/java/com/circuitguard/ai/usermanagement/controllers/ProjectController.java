package com.circuitguard.ai.usermanagement.controllers;

import com.circuitguard.ai.usermanagement.dto.ProjectDTO;
import com.circuitguard.ai.usermanagement.services.ProjectService;
import com.circuitguard.commonservice.dto.StandardResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects")
@AllArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public StandardResponse<ProjectDTO> createProject(@RequestBody ProjectDTO projectDTO) {
        ProjectDTO created = projectService.createProject(projectDTO);
        return StandardResponse.single( "Project created successfully",created);
    }

    @GetMapping("/{projectId}")
    public StandardResponse<ProjectDTO> getProjectById(@PathVariable Long projectId) {
        ProjectDTO project = projectService.getProjectById(projectId);
        return StandardResponse.single("Project fetched successfully",project);
    }

    @GetMapping
    public StandardResponse<Page<ProjectDTO>> getAllProjects(
            Pageable pageable,
            @RequestParam(required = false) Long clientId,
            @RequestParam(required = false) Long managerId,
            @RequestParam(required = false) String status) {

        Page<ProjectDTO> projects = projectService.getAllProjects(pageable, clientId, managerId, status);
        return StandardResponse.page("Projects fetched successfully",projects);
    }

    @DeleteMapping("/{projectId}")
    public StandardResponse<Void> deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
        return StandardResponse.message("Project deleted successfully");
    }

    @GetMapping("/organization/{orgId}")
    public StandardResponse<Page<ProjectDTO>> getProjectsForOrganization(
            @PathVariable Long orgId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProjectDTO> projects = projectService.getProjectsForOrganization(orgId, pageable);
        return StandardResponse.page("Projects fetched successfully", projects);
    }
}
