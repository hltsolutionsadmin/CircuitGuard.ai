package com.circuitguard.ai.usermanagement.repository;

import com.circuitguard.ai.usermanagement.dto.enums.TicketCategory;
import com.circuitguard.ai.usermanagement.dto.enums.TicketPriority;
import com.circuitguard.ai.usermanagement.dto.enums.TicketSeverity;
import com.circuitguard.ai.usermanagement.dto.enums.TicketStatus;
import com.circuitguard.ai.usermanagement.model.TicketModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<TicketModel, Long> {

    Page<TicketModel> findByProjectId(Long projectId, Pageable pageable);

    Page<TicketModel> findByStatus(TicketStatus status, Pageable pageable);

    Page<TicketModel> findByPriority(TicketPriority priority, Pageable pageable);

    Page<TicketModel> findByProjectIdAndStatus(Long projectId, TicketStatus status, Pageable pageable);

    Page<TicketModel> findByProjectIdAndPriority(Long projectId, TicketPriority priority, Pageable pageable);

    Page<TicketModel> findByStatusAndPriority(TicketStatus status, TicketPriority priority, Pageable pageable);

    Page<TicketModel> findByProjectIdAndStatusAndPriority(Long projectId, TicketStatus status, TicketPriority priority, Pageable pageable);


    // 🔹 New filters based on newly added fields:

    // 1. Find by Severity
    Page<TicketModel> findBySeverity(TicketSeverity severity, Pageable pageable);

    // 2. Find by Category
    Page<TicketModel> findByCategory(TicketCategory category, Pageable pageable);

    // 3. Find by Department
    Page<TicketModel> findByDepartment(String department, Pageable pageable);

    // 4. Find by Department and Severity
    Page<TicketModel> findByDepartmentAndSeverity(String department, TicketSeverity severity, Pageable pageable);

    // 5. Find by Incident Code (unique)
    TicketModel findByIncidentCode(String incidentCode);

    // 6. Find all Draft Tickets
    Page<TicketModel> findByIsDraftTrue(Pageable pageable);

    // 7. Find all Archived Tickets
    Page<TicketModel> findByArchivedTrue(Pageable pageable);

    // 8. Find all Active (non-archived, non-draft) tickets by Department
    Page<TicketModel> findByDepartmentAndArchivedFalseAndIsDraftFalse(String department, Pageable pageable);

}
