package com.example.jobportal.messaging.controller;

import com.example.jobportal.messaging.entity.Message;
import com.example.jobportal.messaging.payload.MessageReq;
import com.example.jobportal.messaging.service.ConversationService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final ConversationService conversationService;

    public ChatController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @MessageMapping("/send-message/{conversationId}")
    @SendTo("/topic/conversation/{conversationId}")
    public Message sendMessage(
            @DestinationVariable String conversationId,
            MessageReq messageReq) {

        return conversationService.addMessageAndSave(conversationId, messageReq);
    }
}
