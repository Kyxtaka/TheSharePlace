package com.accountplace.api.repository;

import com.accountplace.api.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {

    Optional<AccountEntity> findByUuid(UUID uuid);

    /** Tous les comptes d'un groupe. */
    List<AccountEntity> findAllByGroupUuid(UUID groupUuid);

    /** Tous les comptes d'un groupe pour une plateforme donnée. */
    List<AccountEntity> findAllByGroupUuidAndPlatformUuid(UUID groupUuid, UUID platformUuid);

    /** Vérifie qu'un compte appartient bien à un groupe (sécurité). */
    boolean existsByUuidAndGroupUuid(UUID accountUuid, UUID groupUuid);

    /** Charge le compte avec sa plateforme et son groupe en une seule requête. */
    @Query("""
            SELECT a FROM AccountEntity a
            LEFT JOIN FETCH a.platform
            LEFT JOIN FETCH a.group
            WHERE a.uuid = :uuid
            """)
    Optional<AccountEntity> findByUuidWithDetails(@Param("uuid") UUID uuid);
}
