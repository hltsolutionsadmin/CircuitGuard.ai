package com.circuitguard.ai.usermanagement.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketCommentDTO {

    private Long id;

    private Long ticketId;

    private Long createdById;

    @NotBlank(message = "Comment cannot be empty")
    private String comment;

    private LocalDateTime createdAt;
}
