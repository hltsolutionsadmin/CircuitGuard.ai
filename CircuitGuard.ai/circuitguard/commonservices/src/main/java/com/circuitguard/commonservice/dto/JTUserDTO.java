package com.circuitguard.commonservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class JTUserDTO {
    private Long id;
    private String username;
    private String email;
    private String url;
    private String fullName;
    private String primaryContact;
    private String type;
    private Long profilePicture;
    private String gender;
    private Long postalCode;
    private String fcmToken;
    private String juviId;
    private LocalDate lastLogOutDate;
    private LocalDate recentActivityDate;

    private AddressDTO address;

    private Set<String> roles;

}
