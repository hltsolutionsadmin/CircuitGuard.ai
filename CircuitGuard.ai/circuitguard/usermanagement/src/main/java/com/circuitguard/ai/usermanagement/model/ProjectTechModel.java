package com.circuitguard.ai.usermanagement.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "PROJECT_TECH_STACK", 
       indexes = {@Index(name = "idx_project_tech", columnList = "PROJECT_ID, TECH")})
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class ProjectTechModel extends GenericModel {

    @Column(name = "TECH", nullable = false)
    private String tech;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECT_ID", nullable = false)
    private ProjectModel project;
}
