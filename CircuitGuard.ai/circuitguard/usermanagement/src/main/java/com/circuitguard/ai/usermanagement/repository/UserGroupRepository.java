package com.circuitguard.ai.usermanagement.repository;

import com.circuitguard.ai.usermanagement.model.UserGroupModel;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroupModel, Long> {

    boolean existsByGroupNameIgnoreCase(String groupName);

    Optional<UserGroupModel> findByProjectId(Long projectId);

    Page<UserGroupModel> findByProjectId(Long projectId, Pageable pageable);

    boolean existsByProject_IdAndGroupNameIgnoreCase(Long projectId, String groupName);

    Optional<UserGroupModel> findByGroupNameAndProject_Id(String groupName, Long projectId);


}
