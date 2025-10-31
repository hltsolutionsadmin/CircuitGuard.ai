package com.circuitguard.ai.usermanagement.repository;

import com.circuitguard.ai.usermanagement.dto.enums.AssignmentRole;
import com.circuitguard.ai.usermanagement.dto.enums.AssignmentTargetType;
import com.circuitguard.ai.usermanagement.model.UserAssignmentModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM USER_ASSIGNMENT_GROUPS WHERE USER_GROUP_ID = :groupId", nativeQuery = true)
    void deleteByUserGroupId(@Param("groupId") Long groupId);


    @Query("""
                SELECT DISTINCT ua
    FROM UserAssignmentModel ua
    JOIN ua.groups g
                JOIN FETCH ua.user u
    WHERE g.id = :groupId
      AND ua.active = true
""")
    Page<UserAssignmentModel> findAssignmentsByGroupId(@Param("groupId") Long groupId, Pageable pageable);


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

    @Query("""
        SELECT DISTINCT ua
        FROM UserAssignmentModel ua
        LEFT JOIN ua.roles r
        WHERE ua.targetId = :targetId
          AND ua.targetType = :targetType
          AND (r IS NULL OR r NOT IN ('CLIENT_ADMIN', 'CLIENT_USER', 'CLIENT_VIEWER', 'CLIENT_MANAGER'))
        """)
    Page<UserAssignmentModel> findNonClientAssignmentsByTarget(
            @Param("targetId") Long targetId,
            @Param("targetType") AssignmentTargetType targetType,
            Pageable pageable
    );
}
