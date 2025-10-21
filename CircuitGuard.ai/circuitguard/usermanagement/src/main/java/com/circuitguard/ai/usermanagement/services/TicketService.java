package com.circuitguard.ai.usermanagement.services;

import com.circuitguard.ai.usermanagement.dto.TicketDTO;
import com.circuitguard.ai.usermanagement.dto.request.TicketRequest;


import java.util.List;

public interface TicketService {

    // Create a new ticket
    TicketDTO createTicket(TicketRequest request);

    // Update existing ticket details
    TicketDTO updateTicket(Long id, TicketRequest request);

    // Get all tickets
    List<TicketDTO> getAllTickets();

    // Get ticket by ID
    TicketDTO getTicketById(Long id);

    // Delete a ticket
    void deleteTicket(Long id);
}
