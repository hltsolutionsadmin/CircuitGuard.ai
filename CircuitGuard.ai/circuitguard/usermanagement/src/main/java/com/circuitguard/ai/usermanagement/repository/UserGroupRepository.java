package com.circuitguard.ai.usermanagement.repository;

import com.circuitguard.ai.usermanagement.dto.enums.TicketPriority;
import com.circuitguard.ai.usermanagement.model.UserGroupModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroupModel, Long> {

    Page<UserGroupModel> findByProjectId(Long projectId, Pageable pageable);

    boolean existsByProject_IdAndGroupNameIgnoreCase(Long projectId, String groupName);

    Optional<UserGroupModel> findByPriorityAndProject_Id(TicketPriority priority, Long projectId);

    boolean existsByPriorityAndProject_Id(TicketPriority priority, Long projectId);

}
