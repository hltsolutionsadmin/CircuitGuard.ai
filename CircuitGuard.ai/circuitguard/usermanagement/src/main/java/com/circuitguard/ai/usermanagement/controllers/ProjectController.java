package com.circuitguard.ai.usermanagement.controllers;

import com.circuitguard.ai.usermanagement.dto.ProjectDTO;
import com.circuitguard.ai.usermanagement.services.ProjectService;
import com.skillrat.commonservice.dto.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping
    public StandardResponse<ProjectDTO> createProject(@RequestBody ProjectDTO projectDTO) {
        ProjectDTO created = projectService.createProject(projectDTO);
        return StandardResponse.single("Project created successfully", created);
    }

    @PutMapping("/{id}")
    public StandardResponse<ProjectDTO> updateProject(
            @PathVariable("id") Long projectId,
            @RequestBody ProjectDTO projectDTO) {
        ProjectDTO updated = projectService.updateProject(projectId, projectDTO);
        return StandardResponse.single("Project updated successfully", updated);
    }

    @GetMapping("/{id}")
    public StandardResponse<ProjectDTO> getProject(@PathVariable("id") Long projectId) {
        ProjectDTO dto = projectService.getProjectById(projectId);
        return StandardResponse.single("Project fetched successfully", dto);
    }

    @GetMapping
    public StandardResponse<Page<ProjectDTO>> getAllProjects(
            @RequestParam(value = "clientId", required = false) Long clientId,
            @RequestParam(value = "managerId", required = false) Long managerId,
            @RequestParam(value = "status", required = false) String status,
            Pageable pageable) {

        Page<ProjectDTO> page = projectService.getAllProjects(pageable, clientId, managerId, status);
        return StandardResponse.page("Projects fetched successfully", page);
    }

    @DeleteMapping("/{id}")
    public StandardResponse<String> deleteProject(@PathVariable("id") Long projectId) {
        projectService.deleteProject(projectId);
        return StandardResponse.message("Project deleted successfully");
    }
}
