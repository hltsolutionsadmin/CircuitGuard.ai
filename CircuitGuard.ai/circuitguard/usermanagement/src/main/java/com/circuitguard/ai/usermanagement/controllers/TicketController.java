package com.circuitguard.ai.usermanagement.controllers;

import com.circuitguard.ai.usermanagement.dto.TicketDTO;
import com.circuitguard.ai.usermanagement.dto.request.TicketRequest;


import com.circuitguard.ai.usermanagement.services.TicketService;
import com.skillrat.commonservice.dto.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    /**
     * 🟩 Create a new ticket
     */
    @PostMapping
    public StandardResponse<TicketDTO> createTicket(@RequestBody TicketRequest request) {
        TicketDTO created = ticketService.createTicket(request);
        return StandardResponse.single("Ticket created successfully", created);
    }

    /**
     * 🟩 Get all tickets
     */
    @GetMapping
    public StandardResponse<List<TicketDTO>> getAllTickets() {
        List<TicketDTO> tickets = ticketService.getAllTickets();
        return StandardResponse.list("Tickets fetched successfully", tickets);
    }

    /**
     * 🟩 Get a single ticket by ID
     */
    @GetMapping("/{id}")
    public StandardResponse<TicketDTO> getTicketById(@PathVariable("id") Long id) {
        TicketDTO ticket = ticketService.getTicketById(id);
        return StandardResponse.single("Ticket fetched successfully", ticket);
    }

    /**
     * 🟩 Update ticket details
     */
    @PutMapping("/{id}")
    public StandardResponse<TicketDTO> updateTicket(
            @PathVariable("id") Long id,
            @RequestBody TicketRequest request) {
        TicketDTO updated = ticketService.updateTicket(id, request);
        return StandardResponse.single("Ticket updated successfully", updated);
    }

    /**
     * 🟩 Delete a ticket
     */
    @DeleteMapping("/{id}")
    public StandardResponse<String> deleteTicket(@PathVariable("id") Long id) {
        ticketService.deleteTicket(id);
        return StandardResponse.message("Ticket deleted successfully");
    }
}
