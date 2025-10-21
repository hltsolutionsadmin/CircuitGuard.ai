package com.circuitguard.ai.usermanagement.repository;

import com.circuitguard.ai.usermanagement.model.OrganizationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<OrganizationModel, Long> {

    // Check if an organization exists by name (case-insensitive)
    Optional<OrganizationModel> findByNameIgnoreCase(String name);

    // Check if active organization exists by ID
    Optional<OrganizationModel> findByIdAndActiveTrue(Long id);

    // Optional: search by domain
    Optional<OrganizationModel> findByDomainNameIgnoreCase(String domainName);

    // Check existence for validation before creating
    boolean existsByNameIgnoreCase(String name);
}
