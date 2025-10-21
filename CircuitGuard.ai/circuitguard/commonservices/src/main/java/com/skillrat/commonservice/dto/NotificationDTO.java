package com.skillrat.commonservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class NotificationDTO {

    private Long id;
    private Date creationTime;
    private Date modificationTime;
    private String message;
    private String title;
    private Long flatId;
    private Long apartmentId;
    private JTUserDTO jtUserDTO;
    private String type;
    private Long userId;
}
