package com.circuitguard.ai.usermanagement.controllers;

import com.circuitguard.ai.usermanagement.dto.OrganizationDTO;
import com.circuitguard.ai.usermanagement.services.OrganizationService;
import com.circuitguard.commonservice.dto.StandardResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;


    @PostMapping
    public StandardResponse<OrganizationDTO> saveOrUpdateOrganization(@Valid @RequestBody OrganizationDTO organizationDTO) {
        OrganizationDTO result = organizationService.saveOrUpdateOrganization(organizationDTO);
        String msg = (organizationDTO.getId() == null) ? "Organization created successfully" : "Organization updated successfully";
        return StandardResponse.single(msg, result);
    }


    @GetMapping("/{id}")
    public StandardResponse<OrganizationDTO> getOrganizationById(@PathVariable Long id) {
        OrganizationDTO result = organizationService.getOrganizationById(id);
        return StandardResponse.single("Organization fetched successfully", result);
    }


    @GetMapping
    public StandardResponse<Page<OrganizationDTO>> getAllOrganizations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean active
    ) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<OrganizationDTO> result = organizationService.getAllOrganizations(pageable, name, active);
        return StandardResponse.page("Organizations fetched successfully", result);
    }


    @DeleteMapping("/{id}")
    public StandardResponse<Void> deactivateOrganization(@PathVariable Long id) {
        organizationService.deactivateOrganization(id);
        return StandardResponse.message("Organization deactivated successfully");
    }
}
