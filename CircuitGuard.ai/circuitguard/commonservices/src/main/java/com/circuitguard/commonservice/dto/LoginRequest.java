package com.circuitguard.commonservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class LoginRequest {
    @NotBlank
    private String primaryContact;

    @NotBlank
    private String otp;

    private String fullName;

    private String emailAddress;

    private Long businessId;

    private String username;

    private String password;


}
