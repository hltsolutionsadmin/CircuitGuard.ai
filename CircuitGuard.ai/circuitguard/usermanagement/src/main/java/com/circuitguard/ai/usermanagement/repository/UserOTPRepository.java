package com.circuitguard.ai.usermanagement.repository;


import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.circuitguard.ai.usermanagement.model.UserOTPModel;

import java.util.List;

@Repository
public interface UserOTPRepository extends JpaRepository<UserOTPModel, Long> {
    List<UserOTPModel> findByEmailAddressAndOtpType(String emailAddress, String otpType, Sort sort);

    void deleteByPrimaryContactAndOtpType(String primaryContact, String otpType);

    UserOTPModel findByOtpTypeAndPrimaryContact(String otpType, String primaryContact);
}
