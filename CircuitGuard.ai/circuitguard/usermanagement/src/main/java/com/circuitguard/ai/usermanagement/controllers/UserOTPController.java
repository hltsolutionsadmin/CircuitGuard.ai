package com.circuitguard.ai.usermanagement.controllers;

import java.io.IOException;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hlt.customerservices.CustomerIntegrationService;
import com.circuitguard.auth.exception.handling.ErrorCode;
import com.circuitguard.auth.exception.handling.HltCustomerException;
import com.circuitguard.customerservices.impl.CustomerIntegrationServiceImpl;
import com.circuitguard.ai.usermanagement.dto.UserOTPDTO;
import com.circuitguard.ai.usermanagement.model.UserModel;
import com.circuitguard.ai.usermanagement.model.UserOTPModel;
import com.circuitguard.ai.usermanagement.services.UserOTPService;
import com.circuitguard.ai.usermanagement.services.UserService;
import com.circuitguard.utils.SRBaseEndpoint;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
@RestController
@RequestMapping(value = "/auth/jtuserotp")
@Slf4j
@RequiredArgsConstructor
public class UserOTPController extends SRBaseEndpoint {

    private final UserOTPService userOTPService;
    private final UserService userService;

    @Value("${otp.trigger:false}")
    private boolean triggerOtp;

    @Value("${otp.static.enabled:true}")
    private boolean staticOtpEnabled;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final DecimalFormat OTP_FORMAT = new DecimalFormat("000000");

    @PostMapping("/trigger/sign-in")
    public ResponseEntity<?> signIn(
            @Valid @RequestBody UserOTPDTO userOtpDto,
            @RequestParam(defaultValue = "true") boolean triggerOtp) throws IOException {

        log.info("Entering sign-in with Primary Contact: {}, OTP Type: {}",
                userOtpDto.getPrimaryContact(), userOtpDto.getOtpType());

        Optional<UserModel> user = userService.findByPrimaryContact(userOtpDto.getPrimaryContact());
        if (user.isEmpty()) {
            throw new HltCustomerException(ErrorCode.USER_NOT_FOUND);
        }

        userOTPService.deleteByPrimaryContactAndOtpType(
                userOtpDto.getPrimaryContact(), userOtpDto.getOtpType());

        UserOTPModel userOtp = generateAndSaveOtp(userOtpDto);
        UserOTPDTO userOTPDTO = new UserOTPDTO(userOtp.getOtp(), userOtp.getCreationTime());

        CustomerIntegrationService jtCustomerIntegration = new CustomerIntegrationServiceImpl();

        if (triggerOtp) {
            log.debug("Triggering OTP for Primary Contact: {}", userOtp.getPrimaryContact());
            jtCustomerIntegration.triggerSMS(userOtp.getPrimaryContact(), userOtp.getOtp());
            userOTPDTO.setOtp(null);
        }

        return new ResponseEntity<>(userOTPDTO, HttpStatus.OK);
    }

    @PostMapping("/trigger/otp")
    public ResponseEntity<UserOTPDTO> newUserSignUp(
            @Valid @RequestBody UserOTPDTO userOtpDto,
            @RequestParam(name = "triggerOtp", required = false) Boolean triggerOtpOverride) throws IOException {

        log.info("Entering newUserSignUp with Primary Contact: {}, OTP Type: {}, triggerOtpOverride={}",
                userOtpDto.getPrimaryContact(), userOtpDto.getOtpType(), triggerOtpOverride);

        userOTPService.deleteByPrimaryContactAndOtpType(
                userOtpDto.getPrimaryContact(), userOtpDto.getOtpType());

        UserOTPModel userOtp = generateAndSaveOtp(userOtpDto);
        UserOTPDTO userOTPDTO = new UserOTPDTO(userOtp.getOtp(), userOtp.getCreationTime());

        // Resolve effective trigger flag
        boolean effectiveTrigger = triggerOtpOverride != null ? triggerOtpOverride : this.triggerOtp;

        log.info("Effective triggerOtp value: {}", effectiveTrigger);

        if (effectiveTrigger) {
            log.debug("Triggering OTP for Primary Contact: {}", userOtp.getPrimaryContact());
            CustomerIntegrationService jtCustomerIntegration = new CustomerIntegrationServiceImpl();
            jtCustomerIntegration.triggerSMS(userOtp.getPrimaryContact(), userOtp.getOtp());
            userOTPDTO.setOtp(null); // mask OTP if triggered
        }

        return ResponseEntity.ok(userOTPDTO);
    }


    private UserOTPModel generateAndSaveOtp(UserOTPDTO userOTPDTO) {
        log.debug("Populating JTUserOTPModel for Primary Contact: {}", userOTPDTO.getPrimaryContact());

        UserOTPModel jtUserOTP = new UserOTPModel();
        jtUserOTP.setChannel(userOTPDTO.getChannel());
        jtUserOTP.setCreationTime(new Date());
        jtUserOTP.setPrimaryContact(userOTPDTO.getPrimaryContact());
        jtUserOTP.setEmailAddress(userOTPDTO.getEmailAddress());
        jtUserOTP.setOtpType(userOTPDTO.getOtpType());

        String otp;
        if (staticOtpEnabled) {
            // Use static OTPs for testing/demo accounts
            Map<String, String> staticOtps = Map.of(
                    "9100881724", "123123",
                    "9700020630", "291120"
            );
            otp = staticOtps.getOrDefault(userOTPDTO.getPrimaryContact(), generateOtp());
            log.debug("Using static OTP [{}] for Primary Contact: {}", otp, userOTPDTO.getPrimaryContact());
        } else {
            otp = generateOtp();
            log.debug("Generated dynamic OTP [{}] for Primary Contact: {}", otp, userOTPDTO.getPrimaryContact());
        }

        jtUserOTP.setOtp(otp);
        return userOTPService.save(jtUserOTP);
    }

    private String generateOtp() {
        int number = SECURE_RANDOM.nextInt(1_000_000);
        return OTP_FORMAT.format(number);
    }
}
