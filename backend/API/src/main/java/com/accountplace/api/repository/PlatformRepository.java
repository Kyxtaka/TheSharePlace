package com.accountplace.api.repository;

import com.accountplace.api.entity.PlatformEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlatformRepository extends JpaRepository<PlatformEntity, Integer> {

    Optional<PlatformEntity> findByUuid(UUID uuid);

    Optional<PlatformEntity> findByName(String name);

    boolean existsByName(String name);
}
