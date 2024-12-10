package com.ppus.psl_user_heartbeat.repository;

import com.ppus.psl_user_heartbeat.Entity.UserAnalytics;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface UserAnalyticsRepo extends CrudRepository<UserAnalytics, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE UserAnalytics ua SET ua.dateTime = :newTime WHERE ua.id = (SELECT u.id FROM UserAnalytics u WHERE u.userId = :userId and u.action = 'Added' order by u.id desc limit 1)")
    int saveTime(@Param("newTime") LocalDateTime newTime, @Param("userId") String userId);

}
