package org.monkey.service;

import org.monkey.entity.Conversation;
import org.monkey.entity.CreateConversationRequest;
import org.monkey.entity.Message;
import org.monkey.service.impl.MessageServiceImpl;

import java.util.List;

public interface ConversationService {
    List<Conversation> findAll();
    List<Conversation> findAll(String loggedInEmail);
    void addConversation(CreateConversationRequest conversation, MessageServiceImpl messageService);
    Conversation findByUUID(String conversationUUID) throws Exception;
    void addMessage(String conversationUUID, Message message);
    List<Message> getMessages(String uuid);
}
