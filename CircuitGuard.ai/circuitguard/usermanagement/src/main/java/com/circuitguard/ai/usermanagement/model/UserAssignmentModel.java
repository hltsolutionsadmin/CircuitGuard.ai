package com.circuitguard.ai.usermanagement.model;

import com.circuitguard.ai.usermanagement.dto.enums.AssignmentTargetType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "USER_ASSIGNMENTS",
        indexes = {
                @Index(name = "idx_user_assignment_user", columnList = "USER_ID"),
                @Index(name = "idx_user_assignment_target", columnList = "TARGET_ID,TARGET_TYPE")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_assignment", columnNames = {"USER_ID", "TARGET_ID", "TARGET_TYPE"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class UserAssignmentModel extends GenericModel {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserModel user;

    @Enumerated(EnumType.STRING)
    @Column(name = "TARGET_TYPE", nullable = false)
    private AssignmentTargetType targetType;

    @Column(name = "TARGET_ID", nullable = false)
    private Long targetId;

    @Column(name = "ROLE")
    private String role;

    @Column(name = "IS_ACTIVE", nullable = false)
    private Boolean active = true;
}
