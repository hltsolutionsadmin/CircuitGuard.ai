//package com.circuitguard.ai.usermanagement.services;
//
//import com.circuitguard.ai.usermanagement.dto.TicketCommentDTO;
//import com.circuitguard.ai.usermanagement.dto.TicketDTO;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//
//public interface TicketService {
//
//    TicketDTO createTicket(TicketDTO ticketDTO);
//
//    TicketDTO getTicketById(Long ticketId);
//
//    Page<TicketDTO> getAllTickets(Pageable pageable, Long projectId, String status, String priority);
//
//    void deleteTicket(Long ticketId);
//
//    TicketCommentDTO addComment(Long ticketId, TicketCommentDTO commentDTO);
//
//}
package com.circuitguard.ai.usermanagement.services;

import com.circuitguard.ai.usermanagement.dto.TicketCommentDTO;
import com.circuitguard.ai.usermanagement.dto.TicketDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TicketService {

    // Existing core operations
    TicketDTO createTicket(TicketDTO ticketDTO);

    TicketDTO getTicketById(Long ticketId);

    Page<TicketDTO> getAllTickets(Pageable pageable, Long projectId, String status, String priority);

    void deleteTicket(Long ticketId);

    TicketCommentDTO addComment(Long ticketId, TicketCommentDTO commentDTO);


    // 🔹 NEW METHODS added based on new fields and repository updates


    Page<TicketDTO> getTicketsByDepartment(String department, Pageable pageable);


    Page<TicketDTO> getTicketsBySeverity(String severity, Pageable pageable);


    Page<TicketDTO> getTicketsByCategory(String category, Pageable pageable);


    TicketDTO getTicketByIncidentCode(String incidentCode);


    Page<TicketDTO> getDraftTickets(Pageable pageable);


    Page<TicketDTO> getArchivedTickets(Pageable pageable);


    Page<TicketDTO> getActiveTicketsByDepartment(String department, Pageable pageable);


}
