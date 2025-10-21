package com.circuitguard.ai.usermanagement.services;

import com.circuitguard.ai.usermanagement.dto.OrganizationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrganizationService {


    OrganizationDTO saveOrUpdateOrganization(OrganizationDTO organizationDTO);

    OrganizationDTO getOrganizationById(Long organizationId);

    Page<OrganizationDTO> getAllOrganizations(Pageable pageable, String name, Boolean active);

    void deactivateOrganization(Long organizationId);
}
