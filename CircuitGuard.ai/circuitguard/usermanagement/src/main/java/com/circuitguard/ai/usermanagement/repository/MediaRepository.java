package com.circuitguard.ai.usermanagement.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.circuitguard.ai.usermanagement.model.MediaModel;

import java.util.List;

@Repository
public interface MediaRepository extends JpaRepository<MediaModel, Long> {

    MediaModel findByCustomerIdAndMediaType(Long userId, String mediaType);
    List<MediaModel> findByCustomerId(Long userId);

//    List<MediaModel> findByB2bUnitModelId(Long businessId);

}
