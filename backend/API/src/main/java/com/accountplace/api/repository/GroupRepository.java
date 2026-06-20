package com.accountplace.api.repository;

import com.accountplace.api.entity.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, Integer> {

    Optional<GroupEntity> findByUuid(UUID uuid);

    Optional<GroupEntity> findByName(String name);

    boolean existsByName(String name);

    /** Tous les groupes auxquels appartient un utilisateur. */
    @Query("""
            SELECT g FROM GroupEntity g
            JOIN g.members gu
            WHERE gu.user.uuid = :userUuid
            """)
    List<GroupEntity> findAllByMemberUuid(@Param("userUuid") UUID userUuid);

    /** Charge le groupe avec ses membres en une seule requête. */
    @Query("SELECT g FROM GroupEntity g LEFT JOIN FETCH g.members WHERE g.uuid = :uuid")
    Optional<GroupEntity> findByUuidWithMembers(@Param("uuid") UUID uuid);
}
