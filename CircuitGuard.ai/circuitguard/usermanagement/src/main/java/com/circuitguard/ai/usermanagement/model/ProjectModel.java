package com.circuitguard.ai.usermanagement.model;

import com.circuitguard.ai.usermanagement.dto.enums.ProjectStatus;
import com.circuitguard.ai.usermanagement.dto.enums.ProjectType;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PROJECTS", indexes = {
        @Index(name = "idx_project_name", columnList = "NAME"),
        @Index(name = "idx_project_status", columnList = "STATUS")
})
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, exclude = {"tickets", "technologyStack", "userAssignments"})
public class ProjectModel extends GenericModel {

    @Column(name = "NAME", nullable = false)
    @EqualsAndHashCode.Include
    private String name;

    @Column(name = "PROJECT_CODE", nullable = false, unique = true, length = 10)
    private String projectCode;

    @Column(name = "DESCRIPTION", length = 2000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENT_ID", nullable = true)
    private UserModel client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECT_MANAGER_ID")
    private UserModel projectManager;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @Column(name = "TARGET_END_DATE")
    private LocalDate targetEndDate;

    @Column(name = "DUE_DATE")
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = true)
    private ProjectStatus status = ProjectStatus.PLANNED;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE")
    private ProjectType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OWNER_ORG_ID", nullable = true)
    private OrganizationModel ownerOrganization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENT_ORG_ID")
    private OrganizationModel clientOrganization;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TicketModel> tickets = new ArrayList<>();

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "TARGET_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @Where(clause = "TARGET_TYPE = 'PROJECT'")
    private List<UserAssignmentModel> userAssignments = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectTechModel> technologyStack = new ArrayList<>();

    @Column(nullable = true)
    private int progressPercentage = 0;

    @Column(name = "BUDGET_RANGE")
    private String budgetRange;

    @Column(name = "EXPECTED_TEAM_SIZE")
    private String expectedTeamSize;

    @Column(name = "ARCHIVED", nullable = false)
    private Boolean archived = false;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserGroupModel> userGroups = new ArrayList<>();
}
