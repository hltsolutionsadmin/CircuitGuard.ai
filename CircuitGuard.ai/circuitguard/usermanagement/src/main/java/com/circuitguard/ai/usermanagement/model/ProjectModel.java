package com.circuitguard.ai.usermanagement.model;

import com.circuitguard.ai.usermanagement.dto.enums.ProjectStatus;
import com.circuitguard.ai.usermanagement.dto.enums.ProjectType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "PROJECTS", indexes = {
        @Index(name = "idx_project_name", columnList = "NAME"),
        @Index(name = "idx_project_status", columnList = "STATUS")
})
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, exclude = {"teamMembers", "tickets"})
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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "PROJECT_TEAM",
            joinColumns = @JoinColumn(name = "PROJECT_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID"))
    private Set<UserModel> teamMembers = new HashSet<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TicketModel> tickets = new ArrayList<>();

    @Column(nullable = false)
    private int progressPercentage = 0;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectTechModel> technologyStack = new ArrayList<>();

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

    public void addTeamMember(UserModel user) {
        this.teamMembers.add(user);
    }

    public void removeTeamMember(UserModel user) {
        this.teamMembers.remove(user);
    }

    public void addTicket(TicketModel ticket) {
        this.tickets.add(ticket);
        ticket.setProject(this);
    }

    public void removeTicket(TicketModel ticket) {
        this.tickets.remove(ticket);
        ticket.setProject(null);
    }

    public void addTechnology(String tech) {
        ProjectTechModel projectTech = new ProjectTechModel();
        projectTech.setTech(tech);
        projectTech.setProject(this);
        this.technologyStack.add(projectTech);
    }

    public void removeTechnology(ProjectTechModel techModel) {
        this.technologyStack.remove(techModel);
        techModel.setProject(null);
    }
}
