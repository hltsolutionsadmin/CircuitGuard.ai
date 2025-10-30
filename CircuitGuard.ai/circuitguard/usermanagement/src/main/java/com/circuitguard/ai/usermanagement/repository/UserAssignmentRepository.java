package com.circuitguard.ai.usermanagement.repository;

import com.circuitguard.ai.usermanagement.dto.enums.AssignmentRole;
import com.circuitguard.ai.usermanagement.dto.enums.AssignmentTargetType;
import com.circuitguard.ai.usermanagement.model.UserAssignmentModel;
import com.circuitguard.ai.usermanagement.model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserAssignmentRepository extends JpaRepository<UserAssignmentModel, Long> {

    Page<UserAssignmentModel> findByTargetIdAndTargetType(Long targetId, AssignmentTargetType targetType, Pageable pageable);

    Page<UserAssignmentModel> findByUserId(Long userId, Pageable pageable);

    Optional<UserAssignmentModel> findByUser_IdAndTargetTypeAndTargetId(
            Long userId,
            AssignmentTargetType targetType,
            Long targetId
    );

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM USER_ASSIGNMENT_GROUPS WHERE USER_GROUP_ID = :groupId", nativeQuery = true)
    void deleteByUserGroupId(@Param("groupId") Long groupId);


    @Query("""
    SELECT ua.user
    FROM UserAssignmentModel ua
    JOIN ua.groups g
    WHERE g.id = :groupId
      AND ua.active = true
""")
    Page<UserModel> findUsersByGroupId(@Param("groupId") Long groupId, Pageable pageable);

    @Query("""
        SELECT ua FROM UserAssignmentModel ua 
        JOIN ua.roles r
        WHERE ua.targetType = :targetType 
          AND ua.targetId = :targetId 
          AND ua.active = true
          AND r IN :roles
    """)
    Page<UserAssignmentModel> findByTargetAndRoles(
            @Param("targetType") AssignmentTargetType targetType,
            @Param("targetId") Long targetId,
            @Param("roles") java.util.Set<AssignmentRole> roles,
            Pageable pageable
    );

    boolean existsByUserId(Long id);
}
