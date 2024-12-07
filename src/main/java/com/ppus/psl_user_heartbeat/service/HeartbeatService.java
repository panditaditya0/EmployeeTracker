package com.ppus.psl_user_heartbeat.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class HeartbeatService {
    private final Map<String, Long> userHeartbeatMap = new ConcurrentHashMap<>();
    private static final long ACTIVE_THRESHOLD = 60 * 1000;

    public void recordHeartbeat(String userId) {
        userHeartbeatMap.put(userId, System.currentTimeMillis());
    }

    public int getActiveUserCount() {
        long currentTime = System.currentTimeMillis();
        return (int) userHeartbeatMap.values().stream()
                .filter(timestamp -> (currentTime - timestamp) <= ACTIVE_THRESHOLD)
                .count();
    }

    public List<String> getActiveUserIds() {
        long currentTime = System.currentTimeMillis();
        return userHeartbeatMap.entrySet().stream()
                .filter(entry -> (currentTime - entry.getValue()) <= ACTIVE_THRESHOLD)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public void cleanupInactiveUsers() {
        long currentTime = System.currentTimeMillis();
        userHeartbeatMap.entrySet().removeIf(entry -> (currentTime - entry.getValue()) > ACTIVE_THRESHOLD);
    }
}