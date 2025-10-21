package com.skillrat.commonservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class MediaDTO {

    private Long id;
    private String url;
    private String name;
    private String description;
    private String extension;
    private Date creationTime;
    private String mediaType;

    public MediaDTO(Long id, String url, String name, String description, String extension, Date creationTime) {
        this.id = id;
        this.url = url;
        this.name = name;
        this.description = description;
        this.extension = extension;
        this.creationTime = creationTime;
    }

    public MediaDTO(String mediaType, String url) {
        this.mediaType = mediaType;
        this.url = url;
    }
}
