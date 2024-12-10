package com.ppus.psl_user_heartbeat.repository;

import com.ppus.psl_user_heartbeat.Entity.UserAnalytics;
import org.springframework.data.repository.CrudRepository;

public interface UserAnalyticsRepo extends CrudRepository<UserAnalytics, Integer> {
}
