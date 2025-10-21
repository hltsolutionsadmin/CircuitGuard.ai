
package com.circuitguard.ai.usermanagement.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.skillrat.auth.JwtUtils;
import com.skillrat.auth.exception.handling.ErrorCode;
import com.skillrat.auth.exception.handling.HltCustomerException;
import com.skillrat.commonservice.dto.LoggedInUser;
import com.skillrat.commonservice.dto.StandardResponse;
import com.skillrat.commonservice.enums.ERole;
import com.circuitguard.ai.usermanagement.dto.request.LoginRequest;
import com.circuitguard.ai.usermanagement.dto.request.RefreshTokenRequest;
import com.circuitguard.ai.usermanagement.dto.request.UsernameLoginRequest;
import com.circuitguard.ai.usermanagement.jwt.JwtResponse;
import com.circuitguard.ai.usermanagement.model.RoleModel;
import com.circuitguard.ai.usermanagement.model.UserModel;
import com.circuitguard.ai.usermanagement.model.UserOTPModel;
import com.circuitguard.ai.usermanagement.services.RoleService;
import com.circuitguard.ai.usermanagement.services.UserOTPService;
import com.circuitguard.ai.usermanagement.services.UserService;
import com.circuitguard.ai.usermanagement.services.impl.UserDetailsServiceImpl;
import com.skillrat.utils.SRBaseEndpoint;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController extends SRBaseEndpoint {

    private static final String SIGN_IN = "SIGNIN";
    private static final String DELIVERY = "DELIVERY";
    private static final long OTP_VALID_DURATION = 15 * 60 * 1000;

    private final UserOTPService userOTPService;
    private final UserService userService;
    private final RoleService roleService;
    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<Object> generateJwt(@Valid @RequestBody LoginRequest loginRequest) throws JsonProcessingException {
        log.info("Login attempt for primary contact: {}", loginRequest.getPrimaryContact());
        loginRequest.setOtpType(SIGN_IN);
        validateOtp(loginRequest);

        UserModel userModel = userDetailsService.loadUserByPrimaryContact(loginRequest.getPrimaryContact());
        if (ObjectUtils.isEmpty(userModel)) {
            userModel = registerNewUser(loginRequest);
        } else {
            log.info("Existing user found: {}", userModel.getPrimaryContact());
        }
        return ResponseEntity.ok(generateJwtResponse(userModel));
    }

    private UserModel registerNewUser(LoginRequest loginRequest) {
        log.info("Registering new user for contact: {}", loginRequest.getPrimaryContact());

        UserModel userModel = new UserModel();
        userModel.setPrimaryContact(loginRequest.getPrimaryContact());
        userModel.setFullName(loginRequest.getFullName());
        userModel.setRecentActivityDate(LocalDate.now());

        if (StringUtils.isNotEmpty(loginRequest.getEmailAddress())) {
            userModel.setEmail(loginRequest.getEmailAddress());
        }



        Set<RoleModel> userRoles = new HashSet<>();
        userRoles.add(roleService.findByErole(ERole.ROLE_USER));
        userModel.setRoles(userRoles);

        userService.saveUser(userModel);
        log.info("New user registered: {}", userModel.getPrimaryContact());
        return userModel;
    }

    private void validateUserUniqueness(String username, String primaryContact, String email) {
        if (userService.findByUsername(username).isPresent()) {
            throw new HltCustomerException(ErrorCode.USER_ALREADY_EXISTS);
        }
        if (userService.findByPrimaryContact(primaryContact).isPresent()) {
            throw new HltCustomerException(ErrorCode.ALREADY_EXISTS, "Mobile number already exists");
        }
        if (StringUtils.isNotEmpty(email) && userService.findByEmail(email) != null) {
            throw new HltCustomerException(ErrorCode.EMAIL_ALREADY_IN_USE);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<StandardResponse<String>> registerUser(@Valid @RequestBody UsernameLoginRequest request) {
        log.info("Registering user: {}", request.getUsername());

        // 1. Validate uniqueness for username, email, contact
        validateUserUniqueness(request.getUsername(), request.getPrimaryContact(), request.getEmail());

        // 2. Build user with encrypted fields (handled by @Convert)
        UserModel newUser = new UserModel();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(request.getPassword());
        newUser.setFullName(request.getFullName());
        newUser.setEmail(request.getEmail());
        newUser.setEmailHash(DigestUtils.sha256Hex(request.getEmail().trim().toLowerCase()));
        newUser.setPrimaryContact(request.getPrimaryContact());
        newUser.setPrimaryContactHash(DigestUtils.sha256Hex(request.getPrimaryContact()));
        newUser.setRecentActivityDate(LocalDate.now());

        // 3. Assign default role
        RoleModel userRole = roleService.findByErole(ERole.ROLE_USER);
        newUser.setRoles(Set.of(userRole));

        // 4. Save user (encryption handled by JPA layer)
        userService.saveUser(newUser);


        // 5. Return response
        return ResponseEntity.ok(StandardResponse.message("User registered successfully"));
    }



    @PostMapping("/login/username")
    public ResponseEntity<Object> loginWithUsername(@Valid @RequestBody UsernameLoginRequest request) throws JsonProcessingException {
        UserModel user = userService.findByUsername(request.getUsername())
                .orElseThrow(() -> new HltCustomerException(ErrorCode.USER_NOT_FOUND));

        if (!request.getPassword().equals(user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        user.setRecentActivityDate(LocalDate.now());
        userService.saveUser(user);

        return ResponseEntity.ok(generateJwtResponse(user));
    }


    @PostMapping("/refreshToken")
    public ResponseEntity<Object> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) throws JsonProcessingException {
        String refreshToken = refreshTokenRequest.getRefreshToken();

        if (StringUtils.isNotBlank(refreshToken) && jwtUtils.validateJwtToken(refreshToken)) {
            LoggedInUser loggedInUser = jwtUtils.getUserFromToken(refreshToken);
            UserModel userModel = userService.findById(loggedInUser.getId());

            userModel.setRecentActivityDate(LocalDate.now());
            userService.saveUser(userModel);

            String newAccessToken = jwtUtils.generateJwtToken(loggedInUser);

            return ResponseEntity.ok(new JwtResponse(
                    newAccessToken,
                    loggedInUser.getId(),
                    loggedInUser.getPrimaryContact(),
                    loggedInUser.getEmail(),
                    new ArrayList<>(loggedInUser.getRoles()),
                    refreshToken
            ));
        }

        throw new HltCustomerException(ErrorCode.TOKEN_PROCESSING_ERROR);
    }

    @PostMapping("/verify")
    public Boolean verifyOtp(@RequestBody LoginRequest loginRequest) {
        loginRequest.setOtpType(DELIVERY);
        try {
            validateOtp(loginRequest);
            return true;
        } catch (Exception e) {
            log.warn("OTP verification failed for contact: {}", loginRequest.getPrimaryContact(), e);
            return false;
        }
    }


    private void validateOtp(LoginRequest loginRequest) {
        String otpType = loginRequest.getOtpType();
        UserOTPModel otpModel = userOTPService.findByOtpTypeAndPrimaryContact(otpType, loginRequest.getPrimaryContact());

        if (otpModel == null) {
            throw new HltCustomerException(ErrorCode.NOT_FOUND);
        }

        if (isOtpExpired(otpModel)) {
            throw new HltCustomerException(ErrorCode.OTP_EXPIRED);
        }

        if (!loginRequest.getOtp().equals(otpModel.getOtp())) {
            throw new BadCredentialsException("Invalid OTP");
        }
    }

    private boolean isOtpExpired(UserOTPModel otpModel) {
        long currentTime = System.currentTimeMillis();
        return (otpModel.getCreationTime().getTime() + OTP_VALID_DURATION) <= currentTime;
    }

    private JwtResponse generateJwtResponse(UserModel userModel) throws JsonProcessingException {
        try {
            LoggedInUser loggedInUser = convertToLoggedInUser(userModel);
            String jwt = jwtUtils.generateJwtToken(loggedInUser);
            String refreshToken = jwtUtils.generateRefreshToken(loggedInUser);

            return new JwtResponse(
                    jwt,
                    loggedInUser.getId(),
                    loggedInUser.getPrimaryContact(),
                    loggedInUser.getEmail(),
                    new ArrayList<>(loggedInUser.getRoles()),
                    refreshToken
            );
        } finally {
            userOTPService.deleteByPrimaryContactAndOtpType(userModel.getPrimaryContact(), SIGN_IN);
            log.info("Deleted OTP for contact: {}", userModel.getPrimaryContact());
        }
    }

    private LoggedInUser convertToLoggedInUser(UserModel userModel) {
        LoggedInUser user = new LoggedInUser();
        user.setId(userModel.getId());
        user.setPrimaryContact(userModel.getPrimaryContact());
        user.setEmail(userModel.getEmail());
        user.setFullName(userModel.getFullName());

        Set<String> roles = userModel.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());
        user.setRoles(roles);

        return user;
    }
}
