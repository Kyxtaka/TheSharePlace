package com.accountplace.api.repositories;

import com.accountplace.api.entity.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, Integer> {

    /**
     * get the group that correspond the id parsed in parameter
     * @param id
     * @return
     */
    Optional<GroupEntity> findById(int id);

    /**
     * Search every group who contains a given name  in their name
     * @param name String sequance to match with groups names
     * @return List<GroupEntity> List of every group that contain 'name' in their name
     */
    @Query("SELECT G FROM GroupEntity G WHERE G.name LIKE %:name% ")
    List<GroupEntity> findByName(@Param("name") String name);

    /**
     * Check if given uid refers to an actual group
     * @param unique_id Long
     * @return boolean "true" if the given uid match with an actual group, "false" if not
     */
    boolean existsByUID(Long unique_id);
}
