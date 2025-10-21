package com.circuitguard.ai.usermanagement.populator;

import com.circuitguard.ai.usermanagement.dto.MediaDTO;
import com.circuitguard.ai.usermanagement.model.MediaModel;
import com.skillrat.utils.Populator;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class MediaPopulator implements Populator<MediaModel, MediaDTO> {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void populate(MediaModel source, MediaDTO target) {
        if (source == null || target == null) return;

        target.setId(source.getId());
        target.setUrl(source.getUrl());
        target.setFileName(source.getFileName());
        target.setMediaType(source.getMediaType());
        target.setDescription(source.getDescription());
        target.setExtension(source.getExtension());
        target.setActive(source.isActive());
        target.setCreatedBy(source.getCreatedBy());

        if (source.getCreationTime() != null) {
            target.setCreationTime(DATE_FORMAT.format(source.getCreationTime()));
        }
        if (source.getModificationTime() != null) {
            target.setModificationTime(DATE_FORMAT.format(source.getModificationTime()));
        }
    }
}
