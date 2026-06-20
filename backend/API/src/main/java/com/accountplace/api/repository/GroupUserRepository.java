package com.accountplace.api.repository;

import com.accountplace.api.entity.GroupUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupUserRepository extends JpaRepository<GroupUserEntity, Integer> {

    /** Appartenance d'un user à un groupe (peu importe le rôle). */
    boolean existsByGroupUuidAndUserUuid(UUID groupUuid, UUID userUuid);

    /** Récupère l'entrée de membership pour modifier le rôle ou l'expulser. */
    Optional<GroupUserEntity> findByGroupUuidAndUserUuid(UUID groupUuid, UUID userUuid);

    /** Tous les membres d'un groupe avec leurs rôles. */
    @Query("""
            SELECT gu FROM GroupUserEntity gu
            LEFT JOIN FETCH gu.user
            LEFT JOIN FETCH gu.role
            WHERE gu.group.uuid = :groupUuid
            """)
    List<GroupUserEntity> findAllByGroupUuidWithDetails(@Param("groupUuid") UUID groupUuid);

    /** Tous les groupes d'un user avec les détails du groupe. */
    @Query("""
            SELECT gu FROM GroupUserEntity gu
            LEFT JOIN FETCH gu.group
            LEFT JOIN FETCH gu.role
            WHERE gu.user.uuid = :userUuid
            """)
    List<GroupUserEntity> findAllByUserUuidWithDetails(@Param("userUuid") UUID userUuid);

    /** Supprime le membership d'un user dans un groupe (kick / leave). */
    @Modifying
    @Query("DELETE FROM GroupUserEntity gu WHERE gu.group.uuid = :groupUuid AND gu.user.uuid = :userUuid")
    void deleteByGroupUuidAndUserUuid(@Param("groupUuid") UUID groupUuid, @Param("userUuid") UUID userUuid);
}
