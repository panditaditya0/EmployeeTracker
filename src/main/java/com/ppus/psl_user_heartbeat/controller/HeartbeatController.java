package com.ppus.psl_user_heartbeat.controller;

import com.ppus.psl_user_heartbeat.model.HearBeatModel;
import com.ppus.psl_user_heartbeat.model.UsersResponse;
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
    public ResponseEntity<Void> recordHeartbeat(@RequestParam String userId, @RequestParam String currUrl) {
        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        HearBeatModel m = new HearBeatModel();
        m.link = currUrl;
        m.userId = userId;
        heartbeatService.recordHeartbeat(m);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/active-users")
    public ResponseEntity<UsersResponse> getActiveUserIds() {
        List<HearBeatModel> activeUserIds = heartbeatService.getActiveUserIds();
        UsersResponse usersResponse = new UsersResponse();
        usersResponse.count = activeUserIds.stream().count();
        usersResponse.activeUsers = activeUserIds;
        return ResponseEntity.ok(usersResponse);
    }

    @GetMapping("/active-user-count")
    public ResponseEntity<Integer> getActiveUserCount() {
        int activeUserCount = heartbeatService.getActiveUserCount();
        return ResponseEntity.ok(activeUserCount);
    }
}
