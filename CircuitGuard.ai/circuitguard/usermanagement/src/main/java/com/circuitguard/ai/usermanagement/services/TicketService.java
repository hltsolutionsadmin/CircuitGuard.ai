package com.circuitguard.ai.usermanagement.services;

import com.circuitguard.ai.usermanagement.dto.TicketCommentDTO;
import com.circuitguard.ai.usermanagement.dto.TicketDTO;
import com.circuitguard.ai.usermanagement.dto.enums.TicketStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TicketService {

    TicketDTO createOrUpdateTicket(TicketDTO ticketDTO);

    TicketDTO getTicketById(Long ticketId);

    Page<TicketDTO> getAllTickets(Pageable pageable, Long projectId, String status, String priority);

    void deleteTicket(Long ticketId);

    TicketCommentDTO addComment(Long ticketId, TicketCommentDTO commentDTO);

    TicketDTO assignTicket(Long ticketId, Long assigneeId);

    TicketDTO updateTicketStatus(Long ticketId, TicketStatus status);

    Page<TicketDTO> getTicketsForUser(Pageable pageable, Long userId);
}
