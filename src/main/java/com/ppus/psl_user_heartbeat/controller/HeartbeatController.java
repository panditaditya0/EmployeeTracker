package com.ppus.psl_user_heartbeat.controller;

import com.ppus.psl_user_heartbeat.service.HeartbeatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/heartbeat")
public class HeartbeatController {
    private final HeartbeatService heartbeatService;

    public HeartbeatController(HeartbeatService heartbeatService) {
        this.heartbeatService = heartbeatService;
    }

    @PostMapping
    @CrossOrigin
    public ResponseEntity<Void> recordHeartbeat(@RequestParam String userId) {
        heartbeatService.recordHeartbeat(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/active-users")
    public ResponseEntity<List<String>> getActiveUserIds() {
        List<String> activeUserIds = heartbeatService.getActiveUserIds();
        return ResponseEntity.ok(activeUserIds);
    }

    @GetMapping("/active-user-count")
    public ResponseEntity<Integer> getActiveUserCount() {
        int activeUserCount = heartbeatService.getActiveUserCount();
        return ResponseEntity.ok(activeUserCount);
    }
}
