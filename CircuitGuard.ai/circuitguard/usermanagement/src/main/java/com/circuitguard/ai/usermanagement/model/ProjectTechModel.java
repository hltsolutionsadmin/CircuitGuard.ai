package com.circuitguard.ai.usermanagement.model;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "PROJECT_TECH_STACK", indexes = {
        @Index(name = "idx_project_tech", columnList = "PROJECT_ID, TECH")
})
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProjectTechModel extends GenericModel {

    @Column(name = "TECH", nullable = false)
    private String tech;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECT_ID", nullable = false)
    private ProjectModel project;


}
