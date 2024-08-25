package com.accountplace.api.repositories;

import com.accountplace.api.entity.PlatformEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlateformRepository extends JpaRepository<PlatformEntity, Integer> {
    @Query("SELECT P FROM PlatformEntity P WHERE P.name LIKE %:word% OR P.url LIKE %:word% ")
    List<PlatformEntity> findBySearch(String word);

}
