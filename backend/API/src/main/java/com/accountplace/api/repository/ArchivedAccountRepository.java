package com.accountplace.api.repository;

import com.accountplace.api.entity.ArchivedAccountEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ArchivedAccountRepository extends JpaRepository<ArchivedAccountEntity, Integer> {

    Optional<ArchivedAccountEntity> findByUuid(UUID uuid);

    /** Archive paginée d'un groupe (pour les admins GDPR). */
    Page<ArchivedAccountEntity> findAllByGroupId(Integer groupId, Pageable pageable);
}
