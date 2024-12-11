package com.ppus.psl_user_heartbeat.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class HeartbeatCleanupTask {
    private final HeartbeatService heartbeatService;
    private final WsHeartBeatService wsHeartBeatService;

    public HeartbeatCleanupTask(HeartbeatService heartbeatService, WsHeartBeatService wsHeartBeatService) {
        this.heartbeatService = heartbeatService;
        this.wsHeartBeatService = wsHeartBeatService;
    }

    @Scheduled(fixedRate = 1 * 60 * 1000)
    public void cleanUpInactiveUsers() {
        heartbeatService.cleanupInactiveUsers();
        wsHeartBeatService.cleanupInactiveUsers();
    }
}
