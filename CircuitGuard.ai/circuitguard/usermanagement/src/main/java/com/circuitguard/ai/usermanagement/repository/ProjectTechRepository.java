package com.circuitguard.ai.usermanagement.repository;

import com.circuitguard.ai.usermanagement.model.ProjectTechModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectTechRepository extends JpaRepository<ProjectTechModel, Long> {

    List<ProjectTechModel> findByProjectId(Long projectId);

}
