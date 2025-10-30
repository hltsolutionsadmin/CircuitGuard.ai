package com.circuitguard.ai.usermanagement.repository;

import com.circuitguard.ai.usermanagement.dto.enums.TicketPriority;
import com.circuitguard.ai.usermanagement.dto.enums.TicketStatus;
import com.circuitguard.ai.usermanagement.model.ProjectModel;
import com.circuitguard.ai.usermanagement.model.TicketModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketRepository extends JpaRepository<TicketModel, Long> {

    Page<TicketModel> findByProjectId(Long projectId, Pageable pageable);

    Page<TicketModel> findByStatus(TicketStatus status, Pageable pageable);

    Page<TicketModel> findByPriority(TicketPriority priority, Pageable pageable);

    Page<TicketModel> findByProjectIdAndStatus(Long projectId, TicketStatus status, Pageable pageable);

    Page<TicketModel> findByProjectIdAndPriority(Long projectId, TicketPriority priority, Pageable pageable);

    Page<TicketModel> findByStatusAndPriority(TicketStatus status, TicketPriority priority, Pageable pageable);

    Page<TicketModel> findByProjectIdAndStatusAndPriority(Long projectId, TicketStatus status, TicketPriority priority, Pageable pageable);

    Long countByProject(ProjectModel project);

    @Query("SELECT COALESCE(MAX(t.ticketNumber), 0) FROM TicketModel t WHERE t.project.id = :projectId")
    Long getLastTicketNumberByProject(@Param("projectId") Long projectId);
}
