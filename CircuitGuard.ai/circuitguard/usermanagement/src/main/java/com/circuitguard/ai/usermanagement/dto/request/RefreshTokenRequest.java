package com.circuitguard.ai.usermanagement.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class RefreshTokenRequest {

    @NotNull
    @NotNull
    private String refreshToken;

}
