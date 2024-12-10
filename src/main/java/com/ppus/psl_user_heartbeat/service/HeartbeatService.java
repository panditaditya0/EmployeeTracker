package com.ppus.psl_user_heartbeat.service;

import com.ppus.psl_user_heartbeat.Entity.UserAnalytics;
import com.ppus.psl_user_heartbeat.model.HearBeatModel;
import com.ppus.psl_user_heartbeat.repository.UserAnalyticsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class HeartbeatService {
    private final Map<String, HearBeatModel> userHeartbeatMap = new ConcurrentHashMap<>();
    private static final long ACTIVE_THRESHOLD = 60 * 1000 * 10;

    @Autowired
    private UserAnalyticsRepo userAnalyticsRepo;

    public void recordHeartbeat(HearBeatModel model) {
        model.link = model.link.split("key")[0];
        model.timestamp = System.currentTimeMillis();
        if (!userHeartbeatMap.containsKey(model.userId)) {
            UserAnalytics userAnalytics = new UserAnalytics();
            LocalDateTime now = LocalDateTime.now();
            userAnalytics.setUserId(model.userId);
            userAnalytics.setDateTime(now);
            userAnalytics.setLink(model.link);
            userAnalytics.setAction("Added");
            userAnalyticsRepo.save(userAnalytics);
        }
        userHeartbeatMap.put(model.userId, model);
    }

    public int getActiveUserCount() {
        long currentTime = System.currentTimeMillis();
        return (int) userHeartbeatMap.values().stream()
                .filter(m -> (currentTime - m.timestamp) <= ACTIVE_THRESHOLD)
                .count();
    }

    public List<HearBeatModel> getActiveUserIds() {
        long currentTime = System.currentTimeMillis();
        return userHeartbeatMap.entrySet().stream()
                .filter(m -> (currentTime - m.getValue().timestamp) <= ACTIVE_THRESHOLD)
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public void cleanupInactiveUsers() {
        long currentTime = System.currentTimeMillis();
        LocalDateTime localDateTime = Instant.ofEpochMilli(currentTime)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        List<String> listOfInActiveUserIDs = this.getInactiveUsers(currentTime);
        List<UserAnalytics> listOfInActiveUserObjs = new ArrayList<>();
        for (String userId : listOfInActiveUserIDs) {
            listOfInActiveUserObjs.add(new UserAnalytics(userId, "Removed", "", localDateTime));
        }
        if (listOfInActiveUserObjs.size() > 0) {
            userAnalyticsRepo.saveAll(listOfInActiveUserObjs);
        }
        userHeartbeatMap.entrySet().removeIf(entry -> (currentTime - entry.getValue().timestamp) > ACTIVE_THRESHOLD);
    }

    public List<String> getInactiveUsers(long currentTime) {
        return userHeartbeatMap.entrySet().stream()
                .filter(entry -> (currentTime - entry.getValue().timestamp) > ACTIVE_THRESHOLD)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}