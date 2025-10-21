package com.circuitguard.ai.usermanagement.model;

import com.circuitguard.ai.usermanagement.dto.enums.ProjectStatus;
import com.circuitguard.ai.usermanagement.dto.enums.ProjectType;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "PROJECTS", indexes = {
        @Index(name = "idx_project_name", columnList = "NAME"),
        @Index(name = "idx_project_status", columnList = "STATUS")
})
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, exclude = {"teamMembers", "tickets", "technologyStack"})
public class ProjectModel extends GenericModel {

    @Column(name = "NAME", nullable = false)
    @EqualsAndHashCode.Include
    private String name;

    @Column(name = "DESCRIPTION", length = 2000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENT_ID", nullable = false)
    private UserModel client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECT_MANAGER_ID")
    private UserModel projectManager;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private ProjectStatus status = ProjectStatus.PLANNED;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE")
    private ProjectType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OWNER_ORG_ID", nullable = false)
    private OrganizationModel ownerOrganization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENT_ORG_ID")
    private OrganizationModel clientOrganization;



    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TicketModel> tickets = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectTechModel> technologyStack = new ArrayList<>();

    @Column(nullable = false)
    private int progressPercentage = 0;

    @Column(name = "BUDGET_RANGE")
    private String budgetRange;

    @Column(name = "EXPECTED_TEAM_SIZE")
    private String expectedTeamSize;

    @Column(name = "TARGET_END_DATE")
    private LocalDate targetEndDate;

    @Column(name = "DUE_DATE")
    private LocalDate dueDate;

    @Column(name = "ARCHIVED", nullable = false)
    private Boolean archived = false;

}
