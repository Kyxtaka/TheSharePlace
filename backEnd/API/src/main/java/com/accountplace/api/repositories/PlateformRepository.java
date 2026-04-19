package com.accountplace.api.repositories;

import com.accountplace.api.entity.PlatformEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlateformRepository extends JpaRepository<PlatformEntity, Integer> {

    /**
     * Search in DB if the given word match to an actual platform
     * @param word String word to search in platforms name
     * @return List<PlatformEntity> List of every Platform that contains the word in their name
     */
    @Query("SELECT P FROM PlatformEntity P WHERE P.name LIKE %:word% OR P.url LIKE %:word% ")
    List<PlatformEntity> findBySearch(String word);

}
