package com.accountplace.api.repositories;

import com.accountplace.api.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {

    /**
     * Get every account of a certain group
     * @param id Interger group ID
     * @return List<AccountEntity> List of every account of a certain platform and a certain group
     */
    @Query("SELECT C FROM AccountEntity C WHERE C.group_id=:id")
    List<AccountEntity> listByGroupId(@Param("id") Integer id);

    /**
     * Get every account who are in a certain group and filter with a certain platform
     * @param gid Interger group ID
     * @param pid Integer platform ID
     * @return List<AccountEntity> List of every account of a certain platform and a certain group
     */
    @Query("SELECT C FROM AccountEntity C WHERE C.group_id=:gid AND C.platform_id = :pid")
    List<AccountEntity> listByGroupAndPlateformId(@Param("gid") Integer gid, @Param("pid") Integer pid);

    /**
     * Execute prepared query that select every stored account in the database which match to the given email
     * @param  email String email to match to the DB
     * @return List<AccountEntity> List every Account that match with the given email
     */
    @Query("SELECT C FROM AccountEntity C WHERE C.mail LIKE %:mail% ")
    List<AccountEntity> listByMail(@Param("mail") String email);
}
