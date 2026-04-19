package com.accountplace.api.repositories;

import com.accountplace.api.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

//import javax.management.relation.Role;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    Optional<RoleEntity> findByName(String name);

    @Query("SELECT R FROM RoleEntity R WHERE R.name = :name")
    RoleEntity getByName(String name);

}
