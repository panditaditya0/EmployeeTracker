package com.ppus.psl_user_heartbeat.service;

import com.ppus.psl_user_heartbeat.Entity.UserAnalytics;
import com.ppus.psl_user_heartbeat.model.HearBeatModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class WsHeartBeatService {
    private final Map<String, HearBeatModel> userHeartbeatMap = new ConcurrentHashMap<>();
    private static final long ACTIVE_THRESHOLD = 60 * 1000 * 10;

    public void wsRecordHeartbeat(HearBeatModel model) {
        ZoneId istZoneId = ZoneId.of("Asia/Kolkata");

        model.link = model.link.split("key")[0];
        model.timestamp = System.currentTimeMillis();
        LocalDateTime now = LocalDateTime.now(istZoneId);

        if (!userHeartbeatMap.containsKey(model.userId)) {
            UserAnalytics userAnalytics = new UserAnalytics();
            userAnalytics.setUserId(model.userId);
            userAnalytics.setDateTime(now);
            userAnalytics.setLink(model.link);
            userAnalytics.setAction("Added");
            userAnalytics.setFirstPing(now);
//            userAnalyticsRepo.save(userAnalytics);
        } else{
//            userAnalyticsRepo.saveTime(now, model.userId);
        }
        userHeartbeatMap.put(model.userId, model);
    }

    public int getActiveUserCount() {
        long currentTime = System.currentTimeMillis();
        return (int) userHeartbeatMap.values().stream()
                .filter(m -> (currentTime - m.timestamp) <= ACTIVE_THRESHOLD)
                .count();
    }

    public void cleanupInactiveUsers() {
        long currentTime = System.currentTimeMillis();
        userHeartbeatMap.entrySet().removeIf(entry -> (currentTime - entry.getValue().timestamp) > ACTIVE_THRESHOLD);
    }

    public List<HearBeatModel> getActiveUserIds() {
        long currentTime = System.currentTimeMillis();
        return userHeartbeatMap.entrySet().stream()
                .filter(m -> (currentTime - m.getValue().timestamp) <= ACTIVE_THRESHOLD)
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }
}
