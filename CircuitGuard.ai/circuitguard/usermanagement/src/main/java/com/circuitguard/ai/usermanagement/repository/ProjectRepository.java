package com.circuitguard.ai.usermanagement.repository;

import com.circuitguard.ai.usermanagement.model.ProjectModel;
import com.circuitguard.ai.usermanagement.dto.enums.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

  @Repository
  public interface ProjectRepository extends JpaRepository<ProjectModel, Long>, JpaSpecificationExecutor<ProjectModel> {

    Page<ProjectModel> findByOwnerOrganization_IdAndStatus(Long organisationId, ProjectStatus status, Pageable pageable);

    Page<ProjectModel> findByClientId(Long clientId, Pageable pageable);

    Page<ProjectModel> findByProjectManagerId(Long projectManagerId, Pageable pageable);

    Page<ProjectModel> findByClientIdAndProjectManagerId(Long clientId, Long managerId, Pageable pageable);

    Page<ProjectModel> findByClientIdAndStatus(Long clientId, ProjectStatus status, Pageable pageable);

    Page<ProjectModel> findByProjectManagerIdAndStatus(Long managerId, ProjectStatus status, Pageable pageable);

    Page<ProjectModel> findByClientIdAndProjectManagerIdAndStatus(
            Long clientId, Long managerId, ProjectStatus status, Pageable pageable);

    boolean existsByName(String name);

    @Query("SELECT p FROM ProjectModel p WHERE p.ownerOrganization.id = :orgId OR p.clientOrganization.id = :orgId")
    Page<ProjectModel> findByOrganization(@Param("orgId") Long organizationId, Pageable pageable);

    Optional<ProjectModel> findById(Long targetId);

    boolean existsByProjectCode(String projectCode);

    @Query("SELECT COUNT(p) FROM ProjectModel p")
    long countAllProjects();



    @Query("SELECT COUNT(p) FROM ProjectModel p WHERE p.status = com.circuitguard.ai.usermanagement.dto.enums.ProjectStatus.COMPLETED")
    long countCompletedProjects();
      @Query("SELECT COUNT(p) FROM ProjectModel p WHERE p.status = com.circuitguard.ai.usermanagement.dto.enums.ProjectStatus.ON_HOLD")
      long countOnHoldProjects();

      Page<ProjectModel> findByIdIn(List<Long> ids, Pageable pageable);

  }
