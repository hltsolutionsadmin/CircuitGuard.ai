package com.circuitguard.ai.usermanagement.repository;

import com.skillrat.commonservice.enums.ERole;
import com.circuitguard.ai.usermanagement.model.RoleModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<RoleModel, Long> {
    @Query("SELECT r FROM RoleModel r WHERE r.name = :name")
    Optional<RoleModel> findByName(@Param("name") ERole name);

    Set<RoleModel> findByNameIn(Set<ERole> names);
}
