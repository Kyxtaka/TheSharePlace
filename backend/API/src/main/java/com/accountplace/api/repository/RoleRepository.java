package com.accountplace.api.repository;

import com.accountplace.api.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {

    Optional<RoleEntity> findByUuid(UUID uuid);

    Optional<RoleEntity> findByRoleName(String roleName);

    boolean existsByRoleName(String roleName);
}
