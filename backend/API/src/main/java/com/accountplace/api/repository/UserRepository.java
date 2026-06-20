package com.accountplace.api.repository;

import com.accountplace.api.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findByUuid(UUID uuid);

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    /** Charge l'utilisateur avec ses rôles globaux en une seule requête. */
    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.roles WHERE u.uuid = :uuid")
    Optional<UserEntity> findByUuidWithRoles(@Param("uuid") UUID uuid);

    /** Utile pour le login — cherche par username ou email indifféremment. */
    @Query("SELECT u FROM UserEntity u WHERE u.username = :login OR u.email = :login")
    Optional<UserEntity> findByUsernameOrEmail(@Param("login") String login);
}
