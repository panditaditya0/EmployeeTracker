package com.ppus.psl_user_heartbeat.controller;

import com.ppus.psl_user_heartbeat.model.HearBeatModel;
import com.ppus.psl_user_heartbeat.service.WsHeartBeatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class SendMessageController {

    private final WsHeartBeatService wsHeartBeatService;

    public SendMessageController(WsHeartBeatService wsHeartBeatService) {
        this.wsHeartBeatService = wsHeartBeatService;
    }

    // complete url =  /app/sendMessage
    @MessageMapping("/sendMessage")
//    @SendTo("/topic/productPage")
    public String sendMessage(HearBeatModel  message){
        try{
            if(null == message.userId || null == message.link)
                return "something null";
            wsHeartBeatService.wsRecordHeartbeat(message);
            return "Got ping "+ message.userId;
        } catch (Exception ex){
            return "Error " + ex.getMessage();
        }
    }
}