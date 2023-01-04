package org.monkey.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.WebSocket;
import org.monkey.entity.ActiveUser;
import org.monkey.entity.Conversation;
import org.monkey.entity.Message;
import org.monkey.entity.SentMessage;
import org.monkey.service.ConversationService;
import org.monkey.service.MessageService;
import org.monkey.websocket.ChatServer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class MessageServiceImpl implements MessageService {
    private final ConversationService conversationService;
    private final ObjectMapper objectMapper;
    private final Set<ActiveUser> activeUsers;

    MessageServiceImpl(ConversationService conversationService, ObjectMapper objectMapper) {
        this.conversationService = conversationService;
        this.objectMapper = objectMapper;
        activeUsers = new HashSet<>();
    }

    @Override
    public void handleMessageTransaction(WebSocket conn, String message, ChatServer chatServer) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(message);

        switch(jsonNode.get("namespace").asText()) {
            case "DISCOVERY":
                handleDiscovery(jsonNode.get("payload"), conn);
                break;
            case "MESSAGE":
                handleMessage(jsonNode.get("payload"), conn);
                break;
            case "LOGOUT":
                handleLogout(jsonNode.get("payload"), conn);
                break;
            default:
                return;
        }
    }

    @Override
    public void handleDisconnect(WebSocket conn) {
        activeUsers.stream()
                .filter(user -> user.getWebSocket().equals(conn))
                .findFirst()
                .map(user -> {
                    log.info("User " + user + " has disconnected");
                    activeUsers.remove(user);
                    return user;
                });

        log.info("Currently there are: " + activeUsers.size() + " users active");
    }

    @SneakyThrows
    private void handleDiscovery(JsonNode jsonNode, WebSocket conn) {
        ActiveUser user = new ActiveUser()
                .setEmail(jsonNode.get("email").asText())
                .setName(jsonNode.get("name").asText())
                .setSurname(jsonNode.get("surname").asText())
                .setWebSocket(conn);

        activeUsers.add(user);

        log.info("User " + user.toString() + " has connected");
        log.info("Currently there are: " + activeUsers.size() + " users active");
    }

    @Transactional
    @SneakyThrows
    void handleMessage(JsonNode jsonNode, WebSocket conn) {
        final String conversationUUID = jsonNode.get("uuid").asText();
        final String from = jsonNode.get("from").asText();
        final String text = jsonNode.get("text").asText();

        conversationService.addMessage(conversationUUID, new Message(from, text));

        final Conversation conversation = conversationService.findByUUID(conversationUUID);

        activeUsers.stream()
                .filter(user -> {
                    return conversation.getListOfUsers()
                            .stream()
                            .anyMatch(us -> us.getEmail().equals(user.getEmail()));
                }).forEach(activeUser -> {
                    try {
                        activeUser.getWebSocket().send(objectMapper.writeValueAsString(new SentMessage()
                                .setUuid(conversationUUID)
                                .setFrom(from)
                                .setText(text)
                                .setNAMESPACE("RECEIVE_MESSAGE")
                        ));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                });
    }

    @SneakyThrows
    private void handleLogout(JsonNode jsonNode, WebSocket conn) {

    }

    public Set<ActiveUser> getActiveUsers() {
        return this.activeUsers;
    }
}
