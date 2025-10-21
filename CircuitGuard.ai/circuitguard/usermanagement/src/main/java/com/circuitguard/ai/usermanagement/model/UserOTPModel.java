package com.circuitguard.ai.usermanagement.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "USER_OTP",
        indexes = {@Index(name = "idx_userotpid", columnList = "id", unique = true)})
@Getter
@Setter
public class UserOTPModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "OTP_TYPE")
    private String otpType;

    @Column(name = "CHANNEL")
    private String channel;

    @Column(name = "EMAIL_ADDRESS")
    private String emailAddress;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = true)
    private UserModel userModel;

    @Column(name = "CREATION_TIME")
    private Date creationTime;

    @Column(name = "OTP")
    private String otp;

    @Column(name = "PRIMARY_CONTACT")
    private String primaryContact;


}
