package com.circuitguard.commonservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceResponse {

    private Long id;

    private String description;

    private Long apartmentId;

    private String requestType;

    private String status;

    private Long createdBy;

    private int categoryId;

    private Long addressId;

    private AddressDTO addressDTO;

    private Long assignTo;

    private LocalDateTime assignedOn;

    private LocalDateTime creationTime;

}