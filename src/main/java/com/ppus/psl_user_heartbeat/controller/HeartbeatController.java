package com.ppus.psl_user_heartbeat.controller;

import com.ppus.psl_user_heartbeat.Config.WebSocketEventListener;
import com.ppus.psl_user_heartbeat.model.HearBeatModel;
import com.ppus.psl_user_heartbeat.model.UsersResponse;
import com.ppus.psl_user_heartbeat.service.HeartbeatService;
import com.ppus.psl_user_heartbeat.service.WsHeartBeatService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/heartbeat")
public class HeartbeatController {
    private final HeartbeatService heartbeatService;
    private final WsHeartBeatService wsHeartBeatService;
    private SimpMessagingTemplate messagingTemplate;
    private final WebSocketEventListener webSocketEventListener;


    public HeartbeatController(HeartbeatService heartbeatService, WsHeartBeatService wsHeartBeatService, SimpMessagingTemplate messagingTemplate, WebSocketEventListener webSocketEventListener) {
        this.heartbeatService = heartbeatService;
        this.wsHeartBeatService = wsHeartBeatService;
        this.messagingTemplate = messagingTemplate;
        this.webSocketEventListener = webSocketEventListener;
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

    @GetMapping("/active-users/{senMsg}")
    public ResponseEntity<UsersResponse> getActiveUserIds(@PathVariable String senMsg) {
        List<HearBeatModel> activeUserIds = heartbeatService.getActiveUserIds();
        UsersResponse usersResponse = new UsersResponse();
        usersResponse.count = activeUserIds.stream().count();
        usersResponse.activeUsers = activeUserIds;
        if (Boolean.valueOf(senMsg) == Boolean.TRUE){
            messagingTemplate.convertAndSend("/topic/productPage",usersResponse.count.toString() );

        }
        return ResponseEntity.ok(usersResponse);
    }

    @GetMapping("/ws-active-users")
    public ResponseEntity<UsersResponse> getWsActiveUserIds() {
        List<HearBeatModel> activeUserIds = wsHeartBeatService.getActiveUserIds();
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
    @GetMapping("/active-ws-connections")
    public int getActiveConnections() {
        return webSocketEventListener.getActiveConnections();
    }
}
