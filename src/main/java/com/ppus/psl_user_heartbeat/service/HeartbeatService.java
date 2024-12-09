package com.ppus.psl_user_heartbeat.service;

import com.ppus.psl_user_heartbeat.model.HearBeatModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class HeartbeatService {
    private final Map<String, HearBeatModel> userHeartbeatMap = new ConcurrentHashMap<>();
    private static final long ACTIVE_THRESHOLD = 60 * 1000 * 4;

    public void recordHeartbeat(HearBeatModel model) {
        model.timestamp = System.currentTimeMillis();
        userHeartbeatMap.put(model.userId,model);
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
        userHeartbeatMap.entrySet().removeIf(entry -> (currentTime - entry.getValue().timestamp) > ACTIVE_THRESHOLD);
    }
}