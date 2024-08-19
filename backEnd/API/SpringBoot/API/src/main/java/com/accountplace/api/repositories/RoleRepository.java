package com.accountplace.api.repositories;

import com.accountplace.api.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

//import javax.management.relation.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);
}
