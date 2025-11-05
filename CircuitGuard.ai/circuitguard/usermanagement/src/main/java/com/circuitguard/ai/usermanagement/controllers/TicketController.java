package com.circuitguard.ai.usermanagement.controllers;

import com.circuitguard.ai.usermanagement.dto.TicketDTO;
import com.circuitguard.ai.usermanagement.dto.TicketCommentDTO;
import com.circuitguard.ai.usermanagement.dto.enums.TicketStatus;
import com.circuitguard.ai.usermanagement.services.TicketService;
import com.circuitguard.commonservice.dto.StandardResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickets")
@AllArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public StandardResponse<TicketDTO> createTicket( @RequestBody TicketDTO ticketDTO) {
        TicketDTO created = ticketService.createOrUpdateTicket(ticketDTO);
        return StandardResponse.single("Ticket created successfully", created);
    }

    @GetMapping("/{id}")
    public StandardResponse<TicketDTO> getTicket(@PathVariable Long id) {
        TicketDTO ticket = ticketService.getTicketById(id);
        return StandardResponse.single("Ticket fetched successfully", ticket);
    }

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


    @DeleteMapping("/{id}")
    public StandardResponse<Void> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return StandardResponse.message("Ticket deleted successfully");
    }

    @PostMapping("/{id}/comments")
    public StandardResponse<TicketCommentDTO> addComment(
            @PathVariable Long id,
            @Valid @RequestBody TicketCommentDTO commentDTO
    ) {
        TicketCommentDTO added = ticketService.addComment(id, commentDTO);
        return StandardResponse.single("Comment added successfully", added);
    }


    @PostMapping("/{ticketId}/assign/{assigneeId}")
    public StandardResponse<TicketDTO> assignTicket(
            @PathVariable Long ticketId,
            @PathVariable Long assigneeId) {
        TicketDTO updated = ticketService.assignTicket(ticketId, assigneeId);
        return StandardResponse.single("Ticket assigned successfully", updated);
    }

    @PatchMapping("/{ticketId}/status")
    public StandardResponse<TicketDTO> updateTicketStatus(
            @PathVariable Long ticketId,
            @RequestParam TicketStatus status
    ) {
        TicketDTO updated = ticketService.updateTicketStatus(ticketId, status);
        return StandardResponse.single("Ticket status updated successfully", updated);
    }

    @GetMapping("/by-user")
    public StandardResponse<Page<TicketDTO>> getTicketsByUser(
            Pageable pageable,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) String status
    ) {
        Page<TicketDTO> tickets = ticketService.getTicketsForUser(pageable, userId, projectId, status);
        return StandardResponse.page("User tickets fetched successfully", tickets);
    }
}
