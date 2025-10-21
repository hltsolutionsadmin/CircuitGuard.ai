package com.circuitguard.ai.usermanagement.services.impl;

import com.circuitguard.ai.usermanagement.dto.TicketCommentDTO;
import com.circuitguard.ai.usermanagement.dto.TicketDTO;
import com.circuitguard.ai.usermanagement.dto.enums.TicketPriority;
import com.circuitguard.ai.usermanagement.dto.enums.TicketStatus;
import com.circuitguard.ai.usermanagement.model.ProjectModel;
import com.circuitguard.ai.usermanagement.model.TicketCommentModel;
import com.circuitguard.ai.usermanagement.model.TicketModel;
import com.circuitguard.ai.usermanagement.model.UserModel;
import com.circuitguard.ai.usermanagement.populator.TicketCommentPopulator;
import com.circuitguard.ai.usermanagement.populator.TicketPopulator;
import com.circuitguard.ai.usermanagement.repository.ProjectRepository;
import com.circuitguard.ai.usermanagement.repository.TicketCommentRepository;
import com.circuitguard.ai.usermanagement.repository.TicketRepository;
import com.circuitguard.ai.usermanagement.repository.UserRepository;
import com.circuitguard.ai.usermanagement.services.TicketService;
import com.skillrat.auth.exception.handling.ErrorCode;
import com.skillrat.auth.exception.handling.HltCustomerException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TicketPopulator ticketPopulator;

    private final TicketCommentRepository ticketCommentRepository;
    private final TicketCommentPopulator ticketCommentPopulator;

    @Override
    public TicketDTO createTicket(TicketDTO ticketDTO) {
        TicketModel model = mapDtoToModel(ticketDTO, new TicketModel());
        TicketModel saved = ticketRepository.save(model);
        return ticketPopulator.toDTO(saved);
    }

    @Override
    public TicketDTO getTicketById(Long ticketId) {
        TicketModel model = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new HltCustomerException(ErrorCode.BUSINESS_NOT_FOUND));
        return ticketPopulator.toDTO(model);
    }

    @Override
    public Page<TicketDTO> getAllTickets(Pageable pageable, Long projectId, String statusStr, String priorityStr) {
        Page<TicketModel> page = fetchTicketsWithFilters(pageable, projectId, statusStr, priorityStr);

        List<TicketDTO> dtos = page.getContent().stream()
                .map(ticketPopulator::toDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Override
    public void deleteTicket(Long ticketId) {
        TicketModel model = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new HltCustomerException(ErrorCode.BUSINESS_NOT_FOUND));
        ticketRepository.delete(model);
    }

    @Override
    public TicketCommentDTO addComment(Long ticketId, TicketCommentDTO commentDTO) {
        TicketModel ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new HltCustomerException(ErrorCode.BUSINESS_NOT_FOUND, "Ticket not found"));

        UserModel createdBy = null;
        if (commentDTO.getCreatedById() != null) {
            createdBy = userRepository.findById(commentDTO.getCreatedById())
                    .orElseThrow(() -> new HltCustomerException(ErrorCode.BUSINESS_NOT_FOUND, "User not found"));
        }

        TicketCommentModel comment = new TicketCommentModel();
        comment.setTicket(ticket);
        comment.setComment(commentDTO.getComment());
        comment.setCreatedBy(createdBy);

        TicketCommentModel saved = ticketCommentRepository.save(comment);
        TicketCommentDTO resultDTO = new TicketCommentDTO();
        ticketCommentPopulator.populate(saved, resultDTO);
        return resultDTO;
    }


    private TicketModel mapDtoToModel(TicketDTO dto, TicketModel model) {
        model.setTitle(dto.getTitle());
        model.setDescription(dto.getDescription());
        model.setStatus(dto.getStatus() != null ? dto.getStatus() : TicketStatus.OPEN);
        model.setPriority(dto.getPriority() != null ? dto.getPriority() : TicketPriority.MEDIUM);
        model.setDueDate(dto.getDueDate());
        model.setArchived(dto.getArchived() != null ? dto.getArchived() : false);

        if (dto.getProjectId() != null) {
            ProjectModel project = projectRepository.findById(dto.getProjectId())
                    .orElseThrow(() -> new HltCustomerException(ErrorCode.BUSINESS_NOT_FOUND));
            model.setProject(project);
        } else {
            throw new HltCustomerException(ErrorCode.BUSINESS_VALIDATION_FAILED, "Project ID is required");
        }

        if (dto.getCreatedById() != null) {
            UserModel createdBy = userRepository.findById(dto.getCreatedById())
                    .orElseThrow(() -> new HltCustomerException(ErrorCode.USER_NOT_FOUND));
            model.setCreatedBy(createdBy);
        }

        if (dto.getAssignedToId() != null) {
            UserModel assignedTo = userRepository.findById(dto.getAssignedToId())
                    .orElseThrow(() -> new HltCustomerException(ErrorCode.USER_NOT_FOUND));
            model.setAssignedTo(assignedTo);
        }

        return model;
    }

    private Page<TicketModel> fetchTicketsWithFilters(Pageable pageable, Long projectId, String statusStr, String priorityStr) {
        TicketStatus status = null;
        TicketPriority priority = null;

        if (statusStr != null) {
            try {
                status = TicketStatus.valueOf(statusStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new HltCustomerException(ErrorCode.BUSINESS_VALIDATION_FAILED, "Invalid ticket status: " + statusStr);
            }
        }

        if (priorityStr != null) {
            try {
                priority = TicketPriority.valueOf(priorityStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new HltCustomerException(ErrorCode.BUSINESS_VALIDATION_FAILED, "Invalid ticket priority: " + priorityStr);
            }
        }

        if (projectId != null && status != null && priority != null) {
            return ticketRepository.findByProjectIdAndStatusAndPriority(projectId, status, priority, pageable);
        } else if (projectId != null && status != null) {
            return ticketRepository.findByProjectIdAndStatus(projectId, status, pageable);
        } else if (projectId != null && priority != null) {
            return ticketRepository.findByProjectIdAndPriority(projectId, priority, pageable);
        } else if (status != null && priority != null) {
            return ticketRepository.findByStatusAndPriority(status, priority, pageable);
        } else if (projectId != null) {
            return ticketRepository.findByProjectId(projectId, pageable);
        } else if (status != null) {
            return ticketRepository.findByStatus(status, pageable);
        } else if (priority != null) {
            return ticketRepository.findByPriority(priority, pageable);
        } else {
            return ticketRepository.findAll(pageable);
        }
    }
}
