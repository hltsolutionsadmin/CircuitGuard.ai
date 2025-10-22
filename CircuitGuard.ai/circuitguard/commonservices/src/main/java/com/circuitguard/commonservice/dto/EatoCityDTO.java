package com.circuitguard.commonservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class EatoCityDTO {

    private Long id;

    @NotBlank(message = "name must not be empty")
    @NotNull(message = "name must not be null")
    private String name;

    private String isoCode;

    @NotBlank(message = "Country must not be empty")
    @NotNull(message = "Country must not be null")
    private String country;

    @NotBlank(message = "Region must not be empty")
    @NotNull(message = "Region must not be null")
    private String region;

    private String district;

    private Date creationTime;

    @NotNull(message = "Codes must not be null")
    private List<String> codes;

}
