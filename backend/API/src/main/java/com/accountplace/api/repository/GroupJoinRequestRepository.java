package com.accountplace.api.repository;

import com.accountplace.api.entity.GroupJoinRequestEntity;
import com.accountplace.api.entity.GroupJoinRequestEntity.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupJoinRequestRepository extends JpaRepository<GroupJoinRequestEntity, Integer> {

    Optional<GroupJoinRequestEntity> findByRequestUuid(UUID requestUuid);

    /** Toutes les demandes en attente pour un groupe (vue admin). */
    List<GroupJoinRequestEntity> findAllByGroupUuidAndRequestStatus(UUID groupUuid, RequestStatus status);

    /** Toutes les demandes envoyées par un user. */
    List<GroupJoinRequestEntity> findAllByUserUuid(UUID userUuid);

    /** Vérifie qu'un user n'a pas déjà une demande pending sur ce groupe. */
    boolean existsByUserUuidAndGroupUuidAndRequestStatus(UUID userUuid, UUID groupUuid, RequestStatus status);

    /** Révoque toutes les demandes expirées (à appeler via un @Scheduled). */
    @Modifying
    @Query("""
            UPDATE GroupJoinRequestEntity r
            SET r.revoked = true
            WHERE r.expireAt < :now
            AND r.requestStatus = 'pending'
            AND r.revoked = false
            """)
    int revokeExpired(@Param("now") LocalDateTime now);
}
