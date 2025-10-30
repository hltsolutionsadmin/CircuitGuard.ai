package com.circuitguard.ai.usermanagement.model;

import com.circuitguard.ai.usermanagement.dto.enums.TicketPriority;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "USER_GROUPS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserGroupModel extends GenericModel {

    @Column(name = "GROUP_NAME", nullable = false)
    private String groupName;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECT_ID")
    private ProjectModel project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_LEAD_ID")
    private UserModel groupLead;

    @Enumerated(EnumType.STRING)
    @Column(name = "PRIORITY", nullable = false)
    private TicketPriority priority;


}
