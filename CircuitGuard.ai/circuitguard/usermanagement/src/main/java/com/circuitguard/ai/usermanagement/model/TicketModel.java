package com.circuitguard.ai.usermanagement.model;

import com.circuitguard.ai.usermanagement.dto.enums.TicketPriority;
import com.circuitguard.ai.usermanagement.dto.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TICKETS", indexes = {
        @Index(name = "idx_ticket_status", columnList = "STATUS"),
        @Index(name = "idx_ticket_priority", columnList = "PRIORITY"),
        @Index(name = "idx_ticket_project", columnList = "PROJECT_ID"),
        @Index(name = "idx_ticket_created_by", columnList = "CREATED_BY")
})
@Getter
@Setter
public class TicketModel extends GenericModel {

    @Column(name = "TICKET_ID", unique = true, nullable = true, length = 50)
    private String ticketId;

    @Column(name = "TITLE", nullable = false, length = 500)
    private String title;

    @Column(name = "DESCRIPTION", length = 4000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "PRIORITY", nullable = false)
    private TicketPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private TicketStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECT_ID", nullable = false)
    private ProjectModel project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATED_BY")
    private UserModel createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ASSIGNED_TO")
    private UserModel assignedTo;

    @Column(name = "RESOLVED_AT")
    private LocalDateTime resolvedAt;

    @Column(name = "DUE_DATE")
    private LocalDateTime dueDate;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TicketCommentModel> comments = new ArrayList<>();

    @Column(name = "IS_ARCHIVED")
    private Boolean archived = false;


    }
