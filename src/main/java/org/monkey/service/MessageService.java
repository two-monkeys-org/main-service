package org.monkey.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.java_websocket.WebSocket;
import org.monkey.websocket.ChatServer;

public interface MessageService {
    void handleMessageTransaction(WebSocket conn, String message, ChatServer chatServer) throws JsonProcessingException;
    void handleDisconnect(WebSocket conn);
}
