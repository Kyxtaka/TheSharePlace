package com.accountplace.api.repository;

import com.accountplace.api.entity.AccountHistoryEntity;
import com.accountplace.api.entity.AccountHistoryEntity.ChangeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AccountHistoryRepository extends JpaRepository<AccountHistoryEntity, Integer> {

    /** Historique complet d'un compte, du plus récent au plus ancien. */
    List<AccountHistoryEntity> findAllByAccountUuidOrderByChangedAtDesc(UUID accountUuid);

    /** Version paginée pour les gros historiques. */
    Page<AccountHistoryEntity> findAllByAccountUuid(UUID accountUuid, Pageable pageable);

    /** Filtré par type de changement (created / updated / deleted). */
    List<AccountHistoryEntity> findAllByAccountUuidAndChangeType(UUID accountUuid, ChangeType changeType);
}
