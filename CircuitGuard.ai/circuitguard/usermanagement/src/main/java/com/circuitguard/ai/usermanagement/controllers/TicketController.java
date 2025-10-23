
package com.circuitguard.ai.usermanagement.controllers;

import com.circuitguard.ai.usermanagement.dto.TicketDTO;
import com.circuitguard.ai.usermanagement.dto.TicketCommentDTO;
import com.circuitguard.ai.usermanagement.services.TicketService;
import com.circuitguard.commonservice.dto.StandardResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing support or issue tracking tickets.
 * Handles CRUD operations and comment management for each ticket.
 */
@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    /**
     * Create a new ticket.
     */
    @PostMapping
    public StandardResponse<TicketDTO> createTicket(@Valid @RequestBody TicketDTO ticketDTO) {
        TicketDTO created = ticketService.createTicket(ticketDTO);
        return StandardResponse.single("Ticket created successfully", created);
    }

    /**
     * Fetch a single ticket by ID.
     */
    @GetMapping("/{id}")
    public StandardResponse<TicketDTO> getTicket(@PathVariable Long id) {
        TicketDTO ticket = ticketService.getTicketById(id);
        return StandardResponse.single("Ticket fetched successfully", ticket);
    }

    /**
     * Fetch all tickets with optional filters for project, status, and priority.
     */
    @GetMapping
    public StandardResponse<Page<TicketDTO>> getAllTickets(
            Pageable pageable,
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority
    ) {
        Page<TicketDTO> tickets = ticketService.getAllTickets(pageable, projectId, status, priority);
        return StandardResponse.page("Tickets fetched successfully", tickets);
    }

    /**
     * Update an existing ticket by ID.
     */
//    @PutMapping("/{id}")
//    public StandardResponse<TicketDTO> updateTicket(
//            @PathVariable Long id,
//            @Valid @RequestBody TicketDTO ticketDTO
//    ) {
//        TicketDTO updated = ticketService.updateTicket(id, ticketDTO);
//        return StandardResponse.single("Ticket updated successfully", updated);
//    }

    /**
     * Delete a ticket by ID.
     */
    @DeleteMapping("/{id}")
    public StandardResponse<Void> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return StandardResponse.message("Ticket deleted successfully");
    }

    /**
     * Add a comment to an existing ticket.
     */
    @PostMapping("/{id}/comments")
    public StandardResponse<TicketCommentDTO> addComment(
            @PathVariable Long id,
            @Valid @RequestBody TicketCommentDTO commentDTO
    ) {
        TicketCommentDTO added = ticketService.addComment(id, commentDTO);
        return StandardResponse.single("Comment added successfully", added);
    }

}
