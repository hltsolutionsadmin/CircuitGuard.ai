package com.circuitguard.ai.usermanagement.services.impl;

import com.circuitguard.ai.usermanagement.dto.TicketDTO;
import com.circuitguard.ai.usermanagement.dto.enums.TicketStatus;
import com.circuitguard.ai.usermanagement.dto.request.TicketRequest;
import com.circuitguard.ai.usermanagement.model.ProjectModel;
import com.circuitguard.ai.usermanagement.model.TicketModel;
import com.circuitguard.ai.usermanagement.model.UserModel;
import com.circuitguard.ai.usermanagement.repository.ProjectRepository;
import com.circuitguard.ai.usermanagement.repository.TicketRepository;
import com.circuitguard.ai.usermanagement.repository.UserRepository;
import com.circuitguard.ai.usermanagement.services.TicketService;
import com.skillrat.auth.exception.handling.ErrorCode;
import com.skillrat.auth.exception.handling.HltCustomerException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    // 🟢 1️⃣ Create a new ticket
    @Override
    public TicketDTO createTicket(TicketRequest request) {
        TicketModel model = new TicketModel();

        // Basic ticket info
        model.setTitle(request.getTitle());
        model.setDescription(request.getDescription());
        model.setPriority(request.getPriority());
        model.setStatus(TicketStatus.NEW);  // default when ticket is created
        model.setCreatedAt(LocalDateTime.now());
        model.setUpdatedAt(LocalDateTime.now());
        model.setArchived(false);

        // Link to project
        if (request.getProjectId() != null) {
            ProjectModel project = projectRepository.findById(request.getProjectId())
                    .orElseThrow(() -> new HltCustomerException(ErrorCode.BUSINESS_NOT_FOUND));
            model.setProject(project);
        }

        // Link to creator
        if (request.getCreatedById() != null) {
            UserModel creator = userRepository.findById(request.getCreatedById())
                    .orElseThrow(() -> new HltCustomerException(ErrorCode.BUSINESS_NOT_FOUND));
            model.setCreatedBy(creator);
        }

        // Link to assigned user (optional)
        if (request.getAssignedToId() != null) {
            UserModel assignee = userRepository.findById(request.getAssignedToId())
                    .orElseThrow(() -> new HltCustomerException(ErrorCode.BUSINESS_NOT_FOUND));
            model.setAssignedTo(assignee);
        }

        TicketModel saved = ticketRepository.save(model);
        return toDTO(saved);
    }

    // 🟡 2️⃣ Update ticket
    @Override
    public TicketDTO updateTicket(Long id, TicketRequest request) {
        TicketModel model = ticketRepository.findById(id)
                .orElseThrow(() -> new HltCustomerException(ErrorCode.BUSINESS_NOT_FOUND));

        // Update fields
        model.setTitle(request.getTitle());
        model.setDescription(request.getDescription());
        model.setPriority(request.getPriority());
        model.setUpdatedAt(LocalDateTime.now());

        // Update assignedTo if provided
        if (request.getAssignedToId() != null) {
            UserModel assignee = userRepository.findById(request.getAssignedToId())
                    .orElseThrow(() -> new HltCustomerException(ErrorCode.BUSINESS_NOT_FOUND));
            model.setAssignedTo(assignee);
        }

        TicketModel updated = ticketRepository.save(model);
        return toDTO(updated);
    }

    // 🔵 3️⃣ Get all tickets
    @Override
    public List<TicketDTO> getAllTickets() {
        List<TicketModel> tickets = ticketRepository.findAll();
        return tickets.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // 🟣 4️⃣ Get ticket by ID
    @Override
    public TicketDTO getTicketById(Long id) {
        TicketModel model = ticketRepository.findById(id)
                .orElseThrow(() -> new HltCustomerException(ErrorCode.BUSINESS_NOT_FOUND));
        return toDTO(model);
    }

    // 🔴 5️⃣ Delete ticket
    @Override
    public void deleteTicket(Long id) {
        TicketModel model = ticketRepository.findById(id)
                .orElseThrow(() -> new HltCustomerException(ErrorCode.BUSINESS_NOT_FOUND));
        ticketRepository.delete(model);
    }

    // 🎯 Utility: Convert TicketModel → TicketDTO
    private TicketDTO toDTO(TicketModel model) {
        TicketDTO dto = new TicketDTO();
        dto.setId(model.getId());
        dto.setTitle(model.getTitle());
        dto.setDescription(model.getDescription());
        dto.setPriority(model.getPriority());
        dto.setStatus(model.getStatus());
        dto.setCreatedAt(model.getCreatedAt());
        dto.setUpdatedAt(model.getUpdatedAt());

        // set readable names
        if (model.getProject() != null) {
            dto.setProjectName(model.getProject().getName());
        }
        if (model.getCreatedBy() != null) {
            dto.setCreatedBy(model.getCreatedBy().getFullName());
        }
        if (model.getAssignedTo() != null) {
            dto.setAssignedTo(model.getAssignedTo().getFullName());
        }


        return dto;
    }
}
