package com.accountplace.api.repository;

import com.accountplace.api.entity.A2fRequestEntity;
import com.accountplace.api.entity.A2fRequestEntity.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface A2fRequestRepository extends JpaRepository<A2fRequestEntity, Integer> {

    Optional<A2fRequestEntity> findByRequestUuid(UUID requestUuid);

    /** Récupère la demande 2FA active pour un compte (une seule à la fois). */
    Optional<A2fRequestEntity> findByAccountUuidAndRequestStatusAndRevokedFalse(
            UUID accountUuid, RequestStatus status);

    /** Validation par magic token (hashé côté service avant appel). */
    Optional<A2fRequestEntity> findByRequestMagicTokenAndRevokedFalse(String hashedMagicToken);

    /** Validation par PIN (hashé côté service avant appel). */
    Optional<A2fRequestEntity> findByRequestPinTokenAndRevokedFalse(String hashedPinToken);

    /** Révoque toutes les demandes 2FA expirées (à appeler via un @Scheduled). */
    @Modifying
    @Query("""
            UPDATE A2fRequestEntity r
            SET r.revoked = true
            WHERE r.expireAt < :now
            AND r.requestStatus = 'pending'
            AND r.revoked = false
            """)
    int revokeExpired(@Param("now") LocalDateTime now);
}
