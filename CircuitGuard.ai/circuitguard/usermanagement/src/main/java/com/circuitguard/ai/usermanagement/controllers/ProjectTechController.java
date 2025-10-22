
package com.circuitguard.ai.usermanagement.controllers;

import com.circuitguard.ai.usermanagement.dto.ProjectTechDTO;
import com.circuitguard.ai.usermanagement.services.ProjectTechService;
import com.circuitguard.commonservice.dto.StandardResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tech-stack")
@RequiredArgsConstructor
public class ProjectTechController {

    private final ProjectTechService projectTechService;

    @PostMapping
    public StandardResponse<ProjectTechDTO> addTechToProject(
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectTechDTO dto) {

        ProjectTechDTO created = projectTechService.addTechToProject(projectId, dto);
        return StandardResponse.single("Technology added successfully", created);
    }


    @GetMapping
    public StandardResponse<Page<ProjectTechDTO>> getProjectTechStack(
            @PathVariable Long projectId,
            Pageable pageable) {

        Page<ProjectTechDTO> page = projectTechService.getTechStackByProject(projectId, pageable);
        return StandardResponse.page("Tech stack retrieved successfully", page);
    }


    @PutMapping("/{techId}")
    public StandardResponse<ProjectTechDTO> updateTech(
            @PathVariable Long techId,
            @Valid @RequestBody ProjectTechDTO dto) {

        ProjectTechDTO updated = projectTechService.updateTech(techId, dto);
        return StandardResponse.single("Technology updated successfully", updated);
    }


    @DeleteMapping("/{techId}")
    public StandardResponse<Void> deleteTech(@PathVariable Long techId) {
        projectTechService.deleteTech(techId);
        return StandardResponse.message("Technology removed successfully");
    }
}
