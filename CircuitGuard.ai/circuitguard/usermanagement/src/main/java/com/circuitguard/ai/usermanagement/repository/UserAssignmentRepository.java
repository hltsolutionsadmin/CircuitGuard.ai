package com.circuitguard.ai.usermanagement.repository;

import com.circuitguard.ai.usermanagement.dto.enums.AssignmentTargetType;
import com.circuitguard.ai.usermanagement.model.UserAssignmentModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAssignmentRepository extends JpaRepository<UserAssignmentModel, Long> {

    Page<UserAssignmentModel> findByTargetIdAndTargetType(Long targetId, AssignmentTargetType targetType, Pageable pageable);

    Page<UserAssignmentModel> findByUserId(Long userId, Pageable pageable);

    Optional<UserAssignmentModel> findByUser_IdAndTargetTypeAndTargetId(
            Long userId,
            AssignmentTargetType targetType,
            Long targetId
    );
}
