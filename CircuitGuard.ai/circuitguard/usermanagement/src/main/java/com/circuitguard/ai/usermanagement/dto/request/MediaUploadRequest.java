package com.circuitguard.ai.usermanagement.dto.request;

import lombok.Data;

@Data
public class MediaUploadRequest {
    private String url;
    private String timeSlot;
    private String fileName;
    private String mediaType;
    private String description;
    private String extension;
    private boolean active;
}
