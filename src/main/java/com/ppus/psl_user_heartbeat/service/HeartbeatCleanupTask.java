package com.ppus.psl_user_heartbeat.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class HeartbeatCleanupTask {
    private final HeartbeatService heartbeatService;

    public HeartbeatCleanupTask(HeartbeatService heartbeatService) {
        this.heartbeatService = heartbeatService;
    }

    @Scheduled(fixedRate = 1 * 60 * 1000)
    public void cleanUpInactiveUsers() {
        heartbeatService.cleanupInactiveUsers();
    }
}
