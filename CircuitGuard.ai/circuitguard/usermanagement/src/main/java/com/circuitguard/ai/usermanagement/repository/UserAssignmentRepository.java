package com.circuitguard.ai.usermanagement.repository;

import com.circuitguard.ai.usermanagement.dto.enums.AssignmentTargetType;
import com.circuitguard.ai.usermanagement.model.UserAssignmentModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAssignmentRepository extends JpaRepository<UserAssignmentModel, Long> {

    Page<UserAssignmentModel> findByTargetTypeAndTargetIdAndActiveTrue(
            AssignmentTargetType targetType, Long targetId, Pageable pageable);

    Page<UserAssignmentModel> findByUser_IdAndActiveTrue(Long userId, Pageable pageable);

    boolean existsByUser_IdAndTargetTypeAndTargetId(Long userId, AssignmentTargetType targetType, Long targetId);
}
