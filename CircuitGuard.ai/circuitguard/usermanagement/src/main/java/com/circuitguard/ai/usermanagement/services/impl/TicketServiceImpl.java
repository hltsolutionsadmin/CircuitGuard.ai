

package com.circuitguard.ai.usermanagement.services.impl;

import com.circuitguard.ai.usermanagement.dto.TicketCommentDTO;
import com.circuitguard.ai.usermanagement.dto.TicketDTO;
import com.circuitguard.ai.usermanagement.dto.enums.*;
        import com.circuitguard.ai.usermanagement.model.*;
        import com.circuitguard.ai.usermanagement.populator.TicketCommentPopulator;
import com.circuitguard.ai.usermanagement.populator.TicketPopulator;
import com.circuitguard.ai.usermanagement.repository.*;
        import com.circuitguard.ai.usermanagement.services.TicketService;
import com.circuitguard.auth.exception.handling.ErrorCode;
import com.circuitguard.auth.exception.handling.HltCustomerException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for managing Tickets and Comments.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TicketPopulator ticketPopulator;
    private final TicketCommentRepository ticketCommentRepository;
    private final TicketCommentPopulator ticketCommentPopulator;

    // ----------------------------------------------------------------
    //                      CORE CRUD OPERATIONS
    // ----------------------------------------------------------------

    @Override
    public TicketDTO createTicket(TicketDTO ticketDTO) {
        TicketModel model = mapDtoToModel(ticketDTO, new TicketModel());
        TicketModel saved = ticketRepository.save(model);
        return ticketPopulator.toDTO(saved);
    }

    @Override
    public TicketDTO getTicketById(Long ticketId) {
        TicketModel model = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new HltCustomerException(ErrorCode.BUSINESS_NOT_FOUND, "Ticket not found"));
        return ticketPopulator.toDTO(model);
    }

    @Override
    public Page<TicketDTO> getAllTickets(Pageable pageable, Long projectId, String statusStr, String priorityStr) {
        Page<TicketModel> tickets = fetchTicketsWithFilters(pageable, projectId, statusStr, priorityStr);
        return convertToDTOPage(tickets, pageable);
    }

    @Override
    public void deleteTicket(Long ticketId) {
        TicketModel model = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new HltCustomerException(ErrorCode.BUSINESS_NOT_FOUND, "Ticket not found"));
        ticketRepository.delete(model);
    }

    @Override
    public TicketCommentDTO addComment(Long ticketId, TicketCommentDTO commentDTO) {
        TicketModel ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new HltCustomerException(ErrorCode.BUSINESS_NOT_FOUND, "Ticket not found"));

        UserModel createdBy = (commentDTO.getCreatedById() != null)
                ? userRepository.findById(commentDTO.getCreatedById())
                .orElseThrow(() -> new HltCustomerException(ErrorCode.USER_NOT_FOUND, "User not found"))
                : null;

        TicketCommentModel comment = new TicketCommentModel();
        comment.setTicket(ticket);
        comment.setComment(commentDTO.getComment());
        comment.setCreatedBy(createdBy);

        TicketCommentModel saved = ticketCommentRepository.save(comment);

        TicketCommentDTO result = new TicketCommentDTO();
        ticketCommentPopulator.populate(saved, result);
        return result;
    }

    // ----------------------------------------------------------------
    //                     ADDITIONAL FILTER METHODS
    // ----------------------------------------------------------------

    @Override
    public Page<TicketDTO> getTicketsByDepartment(String department, Pageable pageable) {
        Page<TicketModel> page = ticketRepository.findByDepartment(department, pageable);
        return convertToDTOPage(page, pageable);
    }

    @Override
    public Page<TicketDTO> getTicketsBySeverity(String severityStr, Pageable pageable) {
        TicketSeverity severity = parseEnum(severityStr, TicketSeverity.class, "severity");
        Page<TicketModel> page = ticketRepository.findBySeverity(severity, pageable);
        return convertToDTOPage(page, pageable);
    }

    @Override
    public Page<TicketDTO> getTicketsByCategory(String categoryStr, Pageable pageable) {
        TicketCategory category = parseEnum(categoryStr, TicketCategory.class, "category");
        Page<TicketModel> page = ticketRepository.findByCategory(category, pageable);
        return convertToDTOPage(page, pageable);
    }

    @Override
    public TicketDTO getTicketByIncidentCode(String incidentCode) {
        TicketModel model = ticketRepository.findByIncidentCode(incidentCode);
        if (model == null)
            throw new HltCustomerException(ErrorCode.BUSINESS_NOT_FOUND, "No ticket found for incident code: " + incidentCode);
        return ticketPopulator.toDTO(model);
    }

    @Override
    public Page<TicketDTO> getDraftTickets(Pageable pageable) {
        Page<TicketModel> page = ticketRepository.findByIsDraftTrue(pageable);
        return convertToDTOPage(page, pageable);
    }

    @Override
    public Page<TicketDTO> getArchivedTickets(Pageable pageable) {
        Page<TicketModel> page = ticketRepository.findByArchivedTrue(pageable);
        return convertToDTOPage(page, pageable);
    }

    @Override
    public Page<TicketDTO> getActiveTicketsByDepartment(String department, Pageable pageable) {
        Page<TicketModel> page = ticketRepository.findByDepartmentAndArchivedFalseAndIsDraftFalse(department, pageable);
        return convertToDTOPage(page, pageable);
    }

    // ----------------------------------------------------------------
    //                          HELPER METHODS
    // ----------------------------------------------------------------

    private <E extends Enum<E>> E parseEnum(String value, Class<E> enumClass, String fieldName) {
        if (value == null) return null;
        try {
            return Enum.valueOf(enumClass, value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new HltCustomerException(ErrorCode.BUSINESS_VALIDATION_FAILED,
                    "Invalid ticket " + fieldName + ": " + value);
        }
    }

    private Page<TicketDTO> convertToDTOPage(Page<TicketModel> page, Pageable pageable) {
        List<TicketDTO> dtos = page.getContent().stream()
                .map(ticketPopulator::toDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    private TicketModel mapDtoToModel(TicketDTO dto, TicketModel model) {
        model.setTitle(dto.getTitle());
        model.setDescription(dto.getDescription());
        model.setStatus(defaultIfNull(dto.getStatus(), TicketStatus.OPEN));
        model.setPriority(defaultIfNull(dto.getPriority(), TicketPriority.MEDIUM));
        model.setDueDate(dto.getDueDate());
        model.setArchived(defaultIfNull(dto.getArchived(), false));
        model.setDepartment(dto.getDepartment());
        model.setLocation(dto.getLocation());
        model.setIncidentCode(dto.getIncidentCode());
        model.setAffectedServices(dto.getAffectedServices());
        model.setBusinessImpact(dto.getBusinessImpact());
        model.setIsDraft(defaultIfNull(dto.getIsDraft(), false));
        model.setSeverity(dto.getSeverity());
        model.setCategory(dto.getCategory());

        // Project validation
        if (dto.getProjectId() == null)
            throw new HltCustomerException(ErrorCode.BUSINESS_VALIDATION_FAILED, "Project ID is required");

        ProjectModel project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new HltCustomerException(ErrorCode.BUSINESS_NOT_FOUND, "Project not found"));
        model.setProject(project);

        // Created By & Assigned To mapping
        if (dto.getCreatedById() != null) {
            UserModel createdBy = userRepository.findById(dto.getCreatedById())
                    .orElseThrow(() -> new HltCustomerException(ErrorCode.USER_NOT_FOUND, "CreatedBy user not found"));
            model.setCreatedBy(createdBy);
        }

        if (dto.getAssignedToId() != null) {
            UserModel assignedTo = userRepository.findById(dto.getAssignedToId())
                    .orElseThrow(() -> new HltCustomerException(ErrorCode.USER_NOT_FOUND, "AssignedTo user not found"));
            model.setAssignedTo(assignedTo);
        }

        return model;
    }

    private <T> T defaultIfNull(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    private Page<TicketModel> fetchTicketsWithFilters(Pageable pageable, Long projectId, String statusStr, String priorityStr) {
        TicketStatus status = parseEnum(statusStr, TicketStatus.class, "status");
        TicketPriority priority = parseEnum(priorityStr, TicketPriority.class, "priority");

        if (projectId != null && status != null && priority != null)
            return ticketRepository.findByProjectIdAndStatusAndPriority(projectId, status, priority, pageable);
        if (projectId != null && status != null)
            return ticketRepository.findByProjectIdAndStatus(projectId, status, pageable);
        if (projectId != null && priority != null)
            return ticketRepository.findByProjectIdAndPriority(projectId, priority, pageable);
        if (status != null && priority != null)
            return ticketRepository.findByStatusAndPriority(status, priority, pageable);
        if (projectId != null)
            return ticketRepository.findByProjectId(projectId, pageable);
        if (status != null)
            return ticketRepository.findByStatus(status, pageable);
        if (priority != null)
            return ticketRepository.findByPriority(priority, pageable);

        return ticketRepository.findAll(pageable);
    }
}
