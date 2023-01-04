package org.monkey.configuration;

import com.corundumstudio.socketio.SocketIOServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class BeansConfig {

    @Bean
    public SocketIOServer socketIOServer() {
//        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
//        config.setHostname("localhost");
//        config.setPort(9092);
//        SocketIOServer srv = new SocketIOServer(config);
//        srv.addConnectListener(onConnected());
//        srv.addDisconnectListener(onDisconnected());
//        srv.addEventListener("chat", ChatMessage.class, onChatReceived());
//
//        return srv;
        return null;
    }

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
