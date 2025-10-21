package com.circuitguard.ai.usermanagement.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "TICKET_COMMENTS")
@Getter
@Setter
public class TicketCommentModel extends GenericModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TICKET_ID")
    private TicketModel ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATED_BY")
    private UserModel createdBy;

    @Column(name = "COMMENT", length = 2000)
    private String comment;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt = LocalDateTime.now();
}
