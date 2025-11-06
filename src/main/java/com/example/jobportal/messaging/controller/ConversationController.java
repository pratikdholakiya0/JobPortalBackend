package com.example.jobportal.messaging.controller;

import com.example.jobportal.auth.service.JobPortalUserPrincipal;
import com.example.jobportal.messaging.entity.Conversation;
import com.example.jobportal.messaging.entity.Message;
import com.example.jobportal.messaging.service.ConversationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/conversation")
public class ConversationController {

    private final ConversationService conversationService;

    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @GetMapping("/my")
    public ResponseEntity<List<Conversation>> getConversation(@AuthenticationPrincipal JobPortalUserPrincipal principal) {
        if (principal == null) throw new IllegalArgumentException("User is not authenticated");
        List<Conversation> conversation = conversationService.getConversation(principal);
        return ResponseEntity.ok().body(conversation);
    }

    @GetMapping("/{id}/getMessages")
    public ResponseEntity<List<Message>> getMessages(
            @AuthenticationPrincipal JobPortalUserPrincipal principal,
            @PathVariable String id,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "20", required = false) int size) {

        List<Message> paginatedMessage = conversationService.getMessages(principal, id, page, size);
        return ResponseEntity.ok().body(paginatedMessage);
    }
}
