package com.circuitguard.ai.usermanagement.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    private Long id;

    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @Email(message = "Invalid email format")
    private String email;

    private Long profilePicture;

    @Pattern(
            regexp = "^[0-9]{10,15}$",
            message = "Primary contact must contain 10–15 digits"
    )
    private String primaryContact;

    @Pattern(
            regexp = "^(Male|Female|Other)$",
            message = "Gender must be one of: Male, Female, Other"
    )
    private String gender;

    @Pattern(
            regexp = "^[0-9]{5,10}$",
            message = "Postal code must contain 5–10 digits"
    )
    private String postalCode;

    private String fcmToken;

    @Size(max = 50, message = "Juvi ID cannot exceed 50 characters")
    private String juviId;

    private LocalDate lastLogOutDate;
    private LocalDate recentActivityDate;

    private List<AddressDTO> addresses;

    private Long businessId;

    private Set<String> roles;

    @JsonIgnore
    @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
    private String password;

    private Boolean profileCompleted;
}
