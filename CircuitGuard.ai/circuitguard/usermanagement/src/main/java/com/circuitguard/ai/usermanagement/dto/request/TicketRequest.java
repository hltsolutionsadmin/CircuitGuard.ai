
package com.circuitguard.ai.usermanagement.dto.request;

import com.circuitguard.ai.usermanagement.dto.enums.TicketPriority;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketRequest{
    private String title;
    private String description;
    private TicketPriority priority;
    private Long projectId;     // project linked to the ticket
    private Long createdById;   // who created it
    private Long assignedToId;  // initially assigned person (can be null)
}

