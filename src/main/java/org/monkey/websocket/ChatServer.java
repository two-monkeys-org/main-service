package org.monkey.websocket;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.monkey.service.MessageService;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

@Slf4j
public class ChatServer extends WebSocketServer {
    private final MessageService messageService;

    public ChatServer(int port, MessageService messageService) throws UnknownHostException {
        super(new InetSocketAddress(port));
        this.messageService = messageService;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {

        conn.send("OK");

        System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " entered the room!");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        broadcast(conn + " has left the room!");
        messageService.handleDisconnect(conn);
    }

    @SneakyThrows
    @Override
    public void onMessage(WebSocket conn, String message) {
        broadcast(message);
        messageService.handleMessageTransaction(conn, message, this);
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        broadcast(message.array());
        System.out.println(conn + ": " + message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
        if (conn != null) {
        }
    }

    @Override
    public void onStart() {
        log.info("WebSocket server started on port: " + this.getPort());
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);
    }

}
