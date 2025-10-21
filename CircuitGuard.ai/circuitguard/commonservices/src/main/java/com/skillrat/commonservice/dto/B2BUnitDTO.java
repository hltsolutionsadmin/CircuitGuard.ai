package com.skillrat.commonservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class B2BUnitDTO {
    private Long id;

    private String businessName;

    private String businessContactNumber;

    private boolean approved;

    private boolean enabled;

    private Double businessLatitude;

    private Double businessLongitude;

  //  private String categoryName;

    private LocalDateTime creationDate;

    //private UserDTO userDTO;

    private AddressDTO addressDTO;

//    private Set<ProductAttributeResponse> attributes;
}
