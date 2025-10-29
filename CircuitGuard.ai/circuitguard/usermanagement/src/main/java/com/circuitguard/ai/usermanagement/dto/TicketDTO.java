package com.circuitguard.ai.usermanagement.dto;

import com.circuitguard.ai.usermanagement.dto.enums.TicketPriority;
import com.circuitguard.ai.usermanagement.dto.enums.TicketStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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


    private String ticketId;


    private String title;

    private String description;

    private TicketPriority priority;

    private TicketStatus status;

    private Long projectId;

    private Long createdById;

    private String createdByName;

    private Long assignedToId;
    private String assignedToName;

    private LocalDateTime resolvedAt;

    private LocalDateTime dueDate;

    private Boolean archived = false;

    private List<TicketCommentDTO> comments;

    private UserGroupDTO userGroupDTO;

}
