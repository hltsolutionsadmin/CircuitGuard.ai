
package com.circuitguard.ai.usermanagement.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Table(name = "ORGANIZATION", indexes = {
        @Index(name = "idx_org_name", columnList = "NAME", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, exclude = {"users", "projects"})
public class OrganizationModel extends GenericModel {

    @EqualsAndHashCode.Include
    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @Column(name = "DESCRIPTION", length = 1000)
    private String description;

    @Column(name = "DOMAIN_NAME")
    private String domainName;

    @Column(name = "ACTIVE", nullable = false)
    private Boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORGANIZATION_ID")
    private OrganizationModel organization;

    @OneToMany(mappedBy = "ownerOrganization", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectModel> projects = new ArrayList<>();

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserModel> users = new ArrayList<>();
}
