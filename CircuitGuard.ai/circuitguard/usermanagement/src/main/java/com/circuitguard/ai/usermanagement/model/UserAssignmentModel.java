package com.circuitguard.ai.usermanagement.model;

import com.circuitguard.ai.usermanagement.dto.enums.AssignmentRole;
import com.circuitguard.ai.usermanagement.dto.enums.AssignmentTargetType;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "USER_ASSIGNMENTS",
        indexes = {
                @Index(name = "idx_user_assignment_user", columnList = "USER_ID"),
                @Index(name = "idx_user_assignment_target", columnList = "TARGET_ID,TARGET_TYPE")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_assignment_per_project",
                        columnNames = {"USER_ID", "TARGET_ID", "TARGET_TYPE"}
                )
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
    @Column(name = "TARGET_TYPE", nullable = false, length = 50)
    private AssignmentTargetType targetType;

    @Column(name = "TARGET_ID", nullable = false)
    private Long targetId;

    @Column(name = "IS_ACTIVE", nullable = false)
    private Boolean active = true;

    @ElementCollection(fetch = FetchType.EAGER, targetClass = AssignmentRole.class)
    @CollectionTable(
            name = "USER_ASSIGNMENT_ROLES",
            joinColumns = @JoinColumn(name = "USER_ASSIGNMENT_ID")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", nullable = false, length = 50)
    private Set<AssignmentRole> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "USER_ASSIGNMENT_GROUPS",
            joinColumns = @JoinColumn(name = "USER_ASSIGNMENT_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_GROUP_ID")
    )
    private Set<UserGroupModel> groups = new HashSet<>();

    /**
     * Utility method to safely add a group to this assignment.
     * Prevents duplicates and ensures clean linking.
     */
    public void addGroup(UserGroupModel group) {
        if (group != null) {
            this.groups.add(group);
        }
    }

    //remove a group from this assignment.
    public void removeGroup(UserGroupModel group) {
        if (group != null) {
            this.groups.remove(group);
        }
    }
}
