package org.monkey.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.monkey.converter.ConversationConverter;
import org.monkey.entity.*;
import org.monkey.repository.ConversationRepository;
import org.monkey.service.ConversationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {
    private final ConversationRepository conversationRepository;
    private final ConversationConverter conversationConverter;
    private final ObjectMapper objectMapper;

    @Override
    public List<Conversation> findAll() {
        return conversationRepository.findAll();
    }

    @Override
    public List<Conversation> findAll(String loggedInEmail) {
        return conversationRepository.findAll()
                .stream()
                .filter(conversation -> conversation.getListOfUsers()
                        .stream()
                        .anyMatch(user ->user.getEmail().equals(loggedInEmail)))
                .collect(Collectors.toList());
    }

    @Override
    public void addConversation(CreateConversationRequest conversation, MessageServiceImpl messageService) {


        Conversation newConversation = conversationRepository.save(
                conversationConverter.of(conversation)
        );


        messageService.getActiveUsers().stream()
                .filter(user -> {
                    return conversation.getListOfUsers()
                            .stream()
                            .anyMatch(us -> us.getEmail().equals(user.getEmail()));
                }).forEach(activeUser -> {
                    try {
                        activeUser.getWebSocket().send(objectMapper.writeValueAsString(new CreatedConversationMessage()
                                .setUuid(newConversation.getUUID())
                                .setTitle(conversation.getTitle())
                                .setNAMESPACE("NEW_CONVERSATION")
                        ));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                });
    }

    @Override
    public Conversation findByUUID(String conversationUUID) throws Exception {
        return conversationRepository.findById(conversationUUID)
                .orElseThrow(Exception::new);
    }

    @Override
    public void addMessage(String conversationUUID, Message message) {
        Conversation conversation = conversationRepository.findById(conversationUUID)
                .orElse(null);

        conversation.getMessages().add(message);


        conversationRepository.save(conversation);
    }

    @Override
    public List<Message> getMessages(String uuid) {
        return conversationRepository.findById(uuid)
                .map(Conversation::getMessages)
                .orElse(null);      // throw error
    }
}
