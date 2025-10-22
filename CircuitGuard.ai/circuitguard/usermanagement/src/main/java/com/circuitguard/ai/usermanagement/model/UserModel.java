package com.circuitguard.ai.usermanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.*;

import com.circuitguard.auth.EncryptedStringConverter;

@Entity
@Table(
        name = "B2B_USER",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_username", columnNames = {"USERNAME"}),
                @UniqueConstraint(name = "uk_user_email", columnNames = {"EMAIL"}),
                @UniqueConstraint(name = "uk_user_email_hash", columnNames = {"EMAIL_HASH"}),
                @UniqueConstraint(name = "uk_user_primary_contact", columnNames = {"PRIMARY_CONTACT"}),
                @UniqueConstraint(name = "uk_user_primary_contact_hash", columnNames = {"PRIMARY_CONTACT_HASH"})
        },
        indexes = {
                @Index(name = "idx_username", columnList = "USERNAME", unique = true),
                @Index(name = "idx_email", columnList = "EMAIL", unique = true),
                @Index(name = "idx_email_hash", columnList = "EMAIL_HASH", unique = true),
                @Index(name = "idx_primary_contact", columnList = "PRIMARY_CONTACT", unique = true),
                @Index(name = "idx_mobile_hash", columnList = "PRIMARY_CONTACT_HASH", unique = true)
        }
)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class UserModel extends GenericModel {

    @Column(name = "FULL_NAME")
    private String fullName;

    @Size(max = 20)
    @Column(name = "USERNAME")
    private String username;

    @Email
    @Size(max = 50)
    @Column(name = "EMAIL")
    private String email;

    @Column(name = "EMAIL_HASH")
    private String emailHash;

    @NotBlank
    @Column(name = "PRIMARY_CONTACT", nullable = false)
    private String primaryContact;

    @Column(name = "PRIMARY_CONTACT_HASH")
    private String primaryContactHash;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "PROFILE_PICTURE_ID")
    private Long profilePictureId;

    @Column(name = "FCM_TOKEN")
    private String fcmToken;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "USER_ROLES",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID")
    )
    private Set<RoleModel> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AddressModel> addresses = new ArrayList<>();

    @Column(name = "RECENT_ACTIVITY_DATE")
    private LocalDate recentActivityDate;

    @Convert(converter = EncryptedStringConverter.class)
    @Column(name = "PASSWORD")
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORG_ID")
    private OrganizationModel organization;

}
