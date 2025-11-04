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
import org.springframework.data.jpa.repository.EntityGraph;

public interface TicketRepository extends JpaRepository<TicketModel, Long> {

    @EntityGraph(attributePaths = {
            "comments",
            "comments.createdBy",
            "project",
            "createdBy",
            "assignedTo",
            "group",
            "category",
            "subCategory"
    })
    Page<TicketModel> findAllBy(Pageable pageable);

    @EntityGraph(attributePaths = {
            "comments",
            "comments.createdBy",
            "project",
            "createdBy",
            "assignedTo",
            "group",
            "category",
            "subCategory"
    })
    Page<TicketModel> findByStatus(TicketStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {
            "comments",
            "comments.createdBy",
            "project",
            "createdBy",
            "assignedTo",
            "group",
            "category",
            "subCategory"
    })
    Page<TicketModel> findByPriority(TicketPriority priority, Pageable pageable);

    @EntityGraph(attributePaths = {
            "comments",
            "comments.createdBy",
            "project",
            "createdBy",
            "assignedTo",
            "group",
            "category",
            "subCategory"
    })
    Page<TicketModel> findByProjectId(Long projectId, Pageable pageable);

    @EntityGraph(attributePaths = {
            "comments",
            "comments.createdBy",
            "project",
            "createdBy",
            "assignedTo",
            "group",
            "category",
            "subCategory"
    })
    Page<TicketModel> findByProjectIdAndStatus(Long projectId, TicketStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {
            "comments",
            "comments.createdBy",
            "project",
            "createdBy",
            "assignedTo",
            "group",
            "category",
            "subCategory"
    })
    Page<TicketModel> findByProjectIdAndPriority(Long projectId, TicketPriority priority, Pageable pageable);

    @EntityGraph(attributePaths = {
            "comments",
            "comments.createdBy",
            "project",
            "createdBy",
            "assignedTo",
            "group",
            "category",
            "subCategory"
    })
    Page<TicketModel> findByStatusAndPriority(TicketStatus status, TicketPriority priority, Pageable pageable);

    @EntityGraph(attributePaths = {
          "comments",
          "comments.createdBy",
          "project",
          "createdBy",
          "assignedTo",
          "group",
          "category",
          "subCategory"
    })
    Page<TicketModel> findByProjectIdAndStatusAndPriority(Long projectId, TicketStatus status, TicketPriority priority, Pageable pageable);

    Long countByProject(ProjectModel project);

    @Query("SELECT COALESCE(MAX(t.ticketNumber), 0) FROM TicketModel t WHERE t.project.id = :projectId")
    Long getLastTicketNumberByProject(@Param("projectId") Long projectId);

    @EntityGraph(attributePaths = {
            "comments",
            "comments.createdBy",
            "project",
            "createdBy",
            "assignedTo",
            "group"
    })
    @Query("SELECT t FROM TicketModel t WHERE (t.createdBy.id = :userId OR t.assignedTo.id = :userId)")
    Page<TicketModel> findByUserInvolved(@Param("userId") Long userId, Pageable pageable);

    @EntityGraph(attributePaths = {
            "comments",
            "comments.createdBy",
            "project",
            "createdBy",
            "assignedTo",
            "group",
            "category",
            "subCategory"
    })
    @Query("SELECT t FROM TicketModel t WHERE (t.createdBy.id = :userId OR t.assignedTo.id = :userId) AND t.project.id = :projectId")
    Page<TicketModel> findByUserInvolvedAndProjectId(@Param("userId") Long userId,
                                                     @Param("projectId") Long projectId,
                                                     Pageable pageable);
}
