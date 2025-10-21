package com.skillrat.commonservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class PostalCodeDTO {

    private Long id;

    @NotNull(message = "code must not be null")
    @Size(min = 6, max = 6, message = "Postal code must be exactly 6 characters")
    private String code;

    private Date creationTime;

}