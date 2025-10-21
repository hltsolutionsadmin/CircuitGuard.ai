package com.circuitguard.ai.usermanagement.repository;

import com.circuitguard.ai.usermanagement.model.ProjectModel;
import com.circuitguard.ai.usermanagement.dto.enums.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectModel, Long> {

    Page<ProjectModel> findByStatus(ProjectStatus status, Pageable pageable);

    Page<ProjectModel> findByClientId(Long clientId, Pageable pageable);

    Page<ProjectModel> findByProjectManagerId(Long projectManagerId, Pageable pageable);

    Page<ProjectModel> findByClientIdAndProjectManagerId(Long clientId, Long managerId, Pageable pageable);

    Page<ProjectModel> findByClientIdAndStatus(Long clientId, ProjectStatus status, Pageable pageable);

    Page<ProjectModel> findByProjectManagerIdAndStatus(Long managerId, ProjectStatus status, Pageable pageable);

    Page<ProjectModel> findByClientIdAndProjectManagerIdAndStatus(
            Long clientId, Long managerId, ProjectStatus status, Pageable pageable);

    boolean existsByName(String name);
}
