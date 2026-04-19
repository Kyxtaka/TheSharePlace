package com.accountplace.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.accountplace.api.entity.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    /**
     * Finds a user by their email address.
     *
     * @param email The email of the user to search for.
     * @return An {@code Optional} containing the found {@code UserEntity}, or empty if not found.
     */
    Optional<UserEntity> findByEmail(String email);


    /**
     * Finds a user by their username.
     *
     * @param username The username of the user to search for.
     * @return An {@code Optional} containing the found {@code UserEntity}, or empty if not found.
     */
    Optional<UserEntity> findByUsername(String username);

    /**
     * Finds a user by their unique identifier (ID).
     *
     * @param id The ID of the user to search for.
     * @return An {@code Optional} containing the found {@code UserEntity}, or empty if not found.
     */
    Optional<UserEntity> findById(int id);

    /**
     * Checks if a user with the given username exists.
     *
     * @param username The username to check for existence.
     * @return {@code true} if a user with the given username exists, otherwise {@code false}.
     */
    Boolean existsByUsername(String username);

    /**
     * Checks if a user with the given email exists.
     *
     * @param email The email to check for existence.
     * @return {@code true} if a user with the given email exists, otherwise {@code false}.
     */
    Boolean existsByEmail(String email);
}
