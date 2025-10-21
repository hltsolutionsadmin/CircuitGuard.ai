package com.circuitguard.ai.usermanagement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.circuitguard.ai.usermanagement.model.AddressModel;

public interface AddressRepository extends JpaRepository<AddressModel, Long> {
    Page<AddressModel> findByUserId(Long userId, Pageable pageable);

//    @Query("SELECT a FROM AddressModel a " +
//            "WHERE a.user.b2bUnit.id = :businessId")
//    Page<AddressModel> findAddressesByBusinessId(@Param("businessId") Long businessId, Pageable pageable);

    AddressModel findByUserIdAndIsDefaultTrue(Long userId);




}

