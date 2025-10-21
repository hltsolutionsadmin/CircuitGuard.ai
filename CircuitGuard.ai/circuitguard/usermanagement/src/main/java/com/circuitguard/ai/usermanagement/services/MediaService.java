package com.circuitguard.ai.usermanagement.services;


import com.circuitguard.ai.usermanagement.dto.MediaDTO;
import com.circuitguard.ai.usermanagement.model.MediaModel;

public interface MediaService {

    MediaModel saveMedia(MediaModel mediaModel);

    MediaModel findByJtcustomerAndMediaType(Long userId, String mediaType);

    void uploadMedia(Long b2bUnitId, MediaDTO dto);


//    boolean existsProfilePicture(Long id);


}
