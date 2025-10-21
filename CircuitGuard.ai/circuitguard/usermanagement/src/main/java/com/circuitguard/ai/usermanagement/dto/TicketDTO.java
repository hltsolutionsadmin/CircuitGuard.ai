package com.circuitguard.ai.usermanagement.dto;
//
//import com.circuitguard.ai.usermanagement.dto.enums.TicketPriority;
//import com.circuitguard.ai.usermanagement.dto.enums.TicketStatus;
//
//import java.time.LocalDateTime;
//
//public class TicketDTO {
//
//    private Long id;
//    private String title;
//    private String description;
//    private TicketPriority priority;
//    private TicketStatus status;
//    private Long projectId;
//    private String projectName; // optional, useful for frontend
//    private Long createdById;
//    private String createdByName; // optional
//    private Long assignedToId;
//    private String assignedToName; // optional
//    private LocalDateTime createdAt;
//    private LocalDateTime updatedAt;
//    private LocalDateTime resolvedAt;
//    private LocalDateTime dueDate;
//    private Boolean archived;
//
//}



import com.circuitguard.ai.usermanagement.dto.enums.TicketPriority;
import com.circuitguard.ai.usermanagement.dto.enums.TicketStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TicketDTO {
    private Long id;
    private String title;
    private String description;
    private TicketPriority priority;
    private TicketStatus status;
    private String projectName;
    private String createdBy;
    private String assignedTo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

