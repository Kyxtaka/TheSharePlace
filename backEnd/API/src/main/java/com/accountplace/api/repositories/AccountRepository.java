package com.accountplace.api.repositories;

import com.accountplace.api.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {
    @Query("SELECT C FROM AccountEntity C WHERE C.group_id=:id")
    List<AccountEntity> listByGroupId(@Param("id") Integer id);

    @Query("SELECT C FROM AccountEntity C WHERE C.group_id=:gid AND C.platform_id = :pid")
    List<AccountEntity> listByGroupAndPlateformId(@Param("gid") Integer gid, @Param("pid") Integer pid);

    @Query("SELECT C FROM AccountEntity C WHERE C.mail LIKE %:mail% ")
    List<AccountEntity> listByMail(@Param("mail") String mail);
}
