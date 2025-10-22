package com.circuitguard.ai.usermanagement.services;

import com.circuitguard.ai.usermanagement.dto.TicketCommentDTO;
import com.circuitguard.ai.usermanagement.dto.TicketDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TicketService {

    TicketDTO createTicket(TicketDTO ticketDTO);

    TicketDTO getTicketById(Long ticketId);

    Page<TicketDTO> getAllTickets(Pageable pageable, Long projectId, String status, String priority);

    TicketDTO updateTicket(Long ticketId, TicketDTO ticketDTO);

    void deleteTicket(Long ticketId);

    TicketCommentDTO addComment(Long ticketId, TicketCommentDTO commentDTO);

}
