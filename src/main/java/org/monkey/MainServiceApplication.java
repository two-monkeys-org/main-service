package org.monkey;

import org.monkey.service.MessageService;
import org.monkey.websocket.ChatServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.net.UnknownHostException;

@SpringBootApplication
public class MainServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainServiceApplication.class, args);
    }

    @Bean
    ChatServer chatServer(MessageService messageService) throws UnknownHostException {
        return new ChatServer(2137, messageService);
    }

    @Bean
    CommandLineRunner commandLineRunner(ChatServer chatServer) {
        return args -> {
            chatServer.run();
        };
    }
}
