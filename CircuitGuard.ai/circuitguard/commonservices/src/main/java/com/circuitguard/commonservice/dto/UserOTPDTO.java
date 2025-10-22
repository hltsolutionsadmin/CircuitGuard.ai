package com.circuitguard.commonservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserOTPDTO {
    @XmlElement
    private Long id;

    @NotBlank(message = "otpType is mandatory")
    private String otpType;

    @XmlElement
    private String channel;

    private String fullName;

    private String emailAddress;

    @XmlElement
    private Date creationTime;

    @XmlElement
    private String otp;

    @NotBlank(message = "primary contact is mandatory")
    private String primaryContact;

    public UserOTPDTO(String otp, Date creationTime) {
        this.otp = otp;
        this.creationTime = creationTime;
    }
}