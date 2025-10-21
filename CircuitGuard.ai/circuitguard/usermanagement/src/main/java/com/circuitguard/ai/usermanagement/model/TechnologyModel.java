package com.circuitguard.ai.usermanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.*;
@Entity
@Table(name = "TECHNOLOGIES")
@Getter
@Setter
public class TechnologyModel extends GenericModel {

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "technologyStack")
    private Set<ProjectModel> projects = new HashSet<>();
}
