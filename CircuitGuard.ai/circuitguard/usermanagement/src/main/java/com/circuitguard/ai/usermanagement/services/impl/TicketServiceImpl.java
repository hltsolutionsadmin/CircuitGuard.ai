package com.circuitguard.ai.usermanagement.services.impl;

import com.circuitguard.ai.usermanagement.dto.TicketCommentDTO;
import com.circuitguard.ai.usermanagement.dto.TicketDTO;
import com.circuitguard.ai.usermanagement.dto.enums.TicketPriority;
import com.circuitguard.ai.usermanagement.dto.enums.TicketStatus;
import com.circuitguard.ai.usermanagement.model.*;
import com.circuitguard.ai.usermanagement.populator.TicketCommentPopulator;
import com.circuitguard.ai.usermanagement.populator.TicketPopulator;
import com.circuitguard.ai.usermanagement.repository.*;
import com.circuitguard.ai.usermanagement.services.TicketService;
import com.circuitguard.auth.exception.handling.ErrorCode;
import com.circuitguard.auth.exception.handling.HltCustomerException;
import com.circuitguard.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;
    private final TicketCommentRepository ticketCommentRepository;

    private final TicketPopulator ticketPopulator;
    private final TicketCommentPopulator ticketCommentPopulator;


    @Override
    public TicketDTO createTicket(TicketDTO ticketDTO) {
        TicketModel ticketModel = mapDtoToModel(ticketDTO, new TicketModel());

        // Auto-assign for HIGH priority tickets if no assignee provided
        if (ticketModel.getPriority() == TicketPriority.HIGH && ticketModel.getAssignedTo() == null) {
            autoAssignHighPriorityTicket(ticketModel);
        }

        TicketModel saved = ticketRepository.save(ticketModel);
        return ticketPopulator.toDTO(saved);
    }

    @Override
    public TicketDTO getTicketById(Long ticketId) {
        TicketModel model = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new HltCustomerException(ErrorCode.TICKET_NOT_FOUND));
        return ticketPopulator.toDTO(model);
    }

    @Override
    public Page<TicketDTO> getAllTickets(Pageable pageable, Long projectId, String statusStr, String priorityStr) {
        Page<TicketModel> tickets = fetchTicketsWithFilters(pageable, projectId, statusStr, priorityStr);

        List<TicketDTO> dtoList = tickets.getContent().stream()
                .map(ticketPopulator::toDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, tickets.getTotalElements());
    }

    @Override
    public void deleteTicket(Long ticketId) {
        TicketModel ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new HltCustomerException(ErrorCode.TICKET_NOT_FOUND));
        ticketRepository.delete(ticket);
    }

    @Override
    public TicketCommentDTO addComment(Long ticketId, TicketCommentDTO commentDTO) {
        TicketModel ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new HltCustomerException(ErrorCode.TICKET_NOT_FOUND));

        UserModel createdBy = commentDTO.getCreatedById() != null
                ? userRepository.findById(SecurityUtils.getCurrentUserDetails().getId())
                .orElseThrow(() -> new HltCustomerException(ErrorCode.USER_NOT_FOUND))
                : null;

        TicketCommentModel comment = new TicketCommentModel();
        comment.setTicket(ticket);
        comment.setComment(commentDTO.getComment());
        comment.setCreatedBy(createdBy);

        TicketCommentModel saved = ticketCommentRepository.save(comment);
        TicketCommentDTO resultDTO = new TicketCommentDTO();
        ticketCommentPopulator.populate(saved, resultDTO);
        return resultDTO;
    }


    @Override
    public TicketDTO assignTicket(Long ticketId, Long assigneeId) {
        TicketModel ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new HltCustomerException(ErrorCode.TICKET_NOT_FOUND));

        // Already assigned validation
        if (ticket.getAssignedTo() != null && ticket.getAssignedTo().getId().equals(assigneeId)) {
            throw new HltCustomerException(ErrorCode.TICKET_ALREADY_ASSIGNED);
        }

        UserModel assignee = userRepository.findById(assigneeId)
                .orElseThrow(() -> new HltCustomerException(ErrorCode.USER_NOT_FOUND));

        // Role-based assignment check (optional)
        // validateAssignmentPermissions(SecurityUtils.getCurrentUserDetails(), ticket);

        ticket.setAssignedTo(assignee);
        ticket.setStatus(TicketStatus.ASSIGNED);
        return ticketPopulator.toDTO(ticketRepository.save(ticket));
    }


    @Override
    public TicketDTO updateTicketStatus(Long ticketId, TicketStatus status) {
        TicketModel ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new HltCustomerException(ErrorCode.TICKET_NOT_FOUND));

//        Long currentUserId = SecurityUtils.getCurrentUserDetails().getId();
//
//        if (ticket.getAssignedTo() == null || !ticket.getAssignedTo().getId().equals(currentUserId)) {
//            throw new HltCustomerException(ErrorCode.UNAUTHORIZED, "You are not allowed to update this ticket status");
//        }

        if (ticket.getStatus() == TicketStatus.CLOSED) {
            throw new HltCustomerException(ErrorCode.TICKET_ALREADY_CLOSED, "Cannot update a closed ticket");
        }

        ticket.setStatus(status);
        return ticketPopulator.toDTO(ticketRepository.save(ticket));
    }

    private void autoAssignHighPriorityTicket(TicketModel ticket) {
        UserGroupModel group = userGroupRepository.findByProjectId(ticket.getProject().getId())
                .orElseThrow(() -> new HltCustomerException(ErrorCode.GROUP_NOT_FOUND_FOR_PROJECT));

        if (group.getGroupLead() == null) {
            throw new HltCustomerException(ErrorCode.GROUP_LEAD_NOT_ASSIGNED);
        }

        ticket.setAssignedTo(group.getGroupLead());
        ticket.setStatus(TicketStatus.ASSIGNED);
    }

    private TicketModel mapDtoToModel(TicketDTO dto, TicketModel model) {
        model.setTitle(dto.getTitle());
        model.setDescription(dto.getDescription());
        model.setStatus(dto.getStatus() != null ? dto.getStatus() : TicketStatus.OPEN);
        model.setPriority(dto.getPriority() != null ? dto.getPriority() : TicketPriority.LOW);
        model.setDueDate(dto.getDueDate());
        model.setArchived(dto.getArchived() != null ? dto.getArchived() : false);

        ProjectModel project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new HltCustomerException(ErrorCode.PROJECT_NOT_FOUND));
        model.setProject(project);

        UserModel createdBy = userRepository.findById(SecurityUtils.getCurrentUserDetails().getId())
                .orElseThrow(() -> new HltCustomerException(ErrorCode.USER_NOT_FOUND));
        model.setCreatedBy(createdBy);

        if (dto.getAssignedToId() != null) {
            UserModel assignedTo = userRepository.findById(dto.getAssignedToId())
                    .orElseThrow(() -> new HltCustomerException(ErrorCode.USER_NOT_FOUND));
            model.setAssignedTo(assignedTo);
        }

        if (dto.getUserGroupDTO() != null && dto.getUserGroupDTO().getId() != null) {
            UserGroupModel group = userGroupRepository.findById(dto.getUserGroupDTO().getId())
                    .orElseThrow(() -> new HltCustomerException(ErrorCode.GROUP_NOT_FOUND));
            model.setGroup(group);
        }

        return model;
    }

    private Page<TicketModel> fetchTicketsWithFilters(Pageable pageable, Long projectId, String statusStr, String priorityStr) {
        TicketStatus status = parseEnum(statusStr, TicketStatus.class, "Invalid ticket status");
        TicketPriority priority = parseEnum(priorityStr, TicketPriority.class, "Invalid ticket priority");

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

    private <E extends Enum<E>> E parseEnum(String value, Class<E> enumType, String errorMsg) {
        if (value == null) return null;
        try {
            return Enum.valueOf(enumType, value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new HltCustomerException(ErrorCode.BUSINESS_VALIDATION_FAILED, errorMsg + ": " + value);
        }
    }
}
