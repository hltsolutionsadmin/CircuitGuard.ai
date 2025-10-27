package com.circuitguard.ai.usermanagement.dto;

import com.circuitguard.ai.usermanagement.dto.enums.TicketPriority;
import com.circuitguard.ai.usermanagement.dto.enums.TicketStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketDTO {

    private Long id;

    @NotBlank(message = "Ticket ID cannot be blank")
    @Size(max = 50, message = "Ticket ID cannot exceed 50 characters")
    private String ticketId;


    @NotBlank(message = "Ticket title is required")
    private String title;

    private String description;

    @NotNull(message = "Ticket priority is required")
    private TicketPriority priority;

    @NotNull(message = "Ticket status is required")
    private TicketStatus status;

    @NotNull(message = "Project ID is required")
    private Long projectId;

    private Long createdById;

    private Long assignedToId;

    private LocalDateTime resolvedAt;

    private LocalDateTime dueDate;

    private Boolean archived = false;

    private List<TicketCommentDTO> comments;
}
