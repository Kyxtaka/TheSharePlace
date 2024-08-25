package com.accountplace.api.repositories;

import com.accountplace.api.entity.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, Integer> {
    @Query("SELECT G FROM GroupEntity G WHERE G.name LIKE %:name% ")
    List<GroupEntity> findByName(@Param("name") String name);

    boolean existsByUID(Long unique_id);
}
