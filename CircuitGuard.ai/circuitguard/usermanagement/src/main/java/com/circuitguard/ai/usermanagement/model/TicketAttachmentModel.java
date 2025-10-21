package com.circuitguard.ai.usermanagement.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "TICKET_ATTACHMENTS")
@Getter
@Setter
public class TicketAttachmentModel extends GenericModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TICKET_ID")
    private TicketModel ticket;

    @Column(name = "FILE_NAME")
    private String fileName;

    @Column(name = "FILE_PATH")
    private String filePath;

    @Column(name = "UPLOADED_BY")
    private Long uploadedBy;

    @Column(name = "UPLOADED_AT")
    private LocalDateTime uploadedAt = LocalDateTime.now();
}
