package com.circuitguard.ai.usermanagement.services.impl;

import org.springframework.stereotype.Service;

import com.skillrat.auth.exception.handling.ErrorCode;
import com.skillrat.auth.exception.handling.HltCustomerException;
import com.circuitguard.ai.usermanagement.dto.MediaDTO;
import com.circuitguard.ai.usermanagement.model.MediaModel;
import com.circuitguard.ai.usermanagement.populator.MediaPopulator;
import com.circuitguard.ai.usermanagement.repository.MediaRepository;
import com.circuitguard.ai.usermanagement.services.MediaService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {

    private final MediaRepository mediaRepository;
    private final MediaPopulator mediaPopulator;


    @Override
    @Transactional
    public MediaModel saveMedia(MediaModel mediaModel) {
        return mediaRepository.save(mediaModel);
    }

    @Override
    public MediaModel findByJtcustomerAndMediaType(Long userId, String mediaType) {
        return mediaRepository.findByCustomerIdAndMediaType(userId, mediaType);
    }

    @Override
    public void uploadMedia(Long b2bUnitId, MediaDTO dto) {

        MediaModel media = new MediaModel();
//        media.setB2bUnitModel(b2b);
        media.setUrl(dto.getUrl());
        media.setFileName(dto.getFileName());
        media.setMediaType(dto.getMediaType());
        media.setDescription(dto.getDescription());
        media.setExtension(dto.getExtension());
        media.setActive(dto.isActive());
        media.setCreatedBy(dto.getCreatedBy());
        media.setCustomerId(dto.getCustomerId());

        mediaRepository.save(media);
    }
}


