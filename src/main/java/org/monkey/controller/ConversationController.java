package org.monkey.controller;

import lombok.RequiredArgsConstructor;
import org.monkey.entity.Conversation;
import org.monkey.entity.CreateConversationRequest;
import org.monkey.entity.Message;
import org.monkey.service.ConversationService;
import org.monkey.service.impl.MessageServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ConversationController {
    private final ConversationService conversationService;
    private final MessageServiceImpl messageService;

    @GetMapping
    public ResponseEntity<List<Conversation>> getConversations(@RequestHeader("username") String loggedInEmail) {
        return ResponseEntity.ok()
                .header("NAMESPACE", "FETCH_CONVERSATIONS")
                .body(conversationService.findAll(loggedInEmail));
    }

    @PostMapping
    public ResponseEntity<?> addConversation(@RequestBody CreateConversationRequest conversation) {
        conversationService.addConversation(conversation, messageService);

        return ResponseEntity.ok()
                .header("NAMESPACE", "CREATE_CONVERSATION")
                .body(null);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<List<Message>> getMessagesForConversation(@PathVariable String uuid) {
        return ResponseEntity.ok()
                .header("NAMESPACE", "FETCH_MESSAGES")
                .body(conversationService.getMessages(uuid));
    }
}
