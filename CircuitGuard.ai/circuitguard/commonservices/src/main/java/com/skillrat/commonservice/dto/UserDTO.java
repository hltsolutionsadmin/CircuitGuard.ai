package com.skillrat.commonservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.skillrat.commonservice.enums.UserVerificationStatus;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    private Long id;

    private String fullName;

    @Size(max = 20)
    private String username;

    @Size(max = 50)
    @Email
    private String email;

    private Set<Role> roles;

    private String profilePicture;

    @NotBlank
    private String primaryContact;

    private String gender;

    private Date creationTime;

    private String type;

    private boolean isRegistered;
    private List<SchemeInfoDTO> schemeInfoList;

    private Long userRegistrationId;

    private String token;

    private int version;

    private String fcmToken;

    private String juviId;

    private String rollNumber;
    private String qualification;

    private String branch;
    private List<MediaDTO> media;
    private UserVerificationStatus userVerificationStatus;
    private Integer studentStartYear;
    private Integer studentEndYear;
    private Long currentYear;
    private String password;
    private Boolean profileCompleted;
}
