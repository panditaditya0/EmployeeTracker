package com.ppus.psl_user_heartbeat.Config;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class WebSocketEventListener {

    private final AtomicInteger activeConnections = new AtomicInteger(0);

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        int currentCount = activeConnections.incrementAndGet();
        System.out.println("New WebSocket connection established. Total active connections: " + currentCount);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        int currentCount = activeConnections.decrementAndGet();
        System.out.println("WebSocket connection closed. Total active connections: " + currentCount);
    }

    public int getActiveConnections() {
        return activeConnections.get();
    }
}
