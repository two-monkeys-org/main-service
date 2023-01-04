package org.monkey.converter;

import lombok.NoArgsConstructor;
import org.monkey.entity.Conversation;
import org.monkey.entity.CreateConversationRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@NoArgsConstructor
@Component
public class ConversationConverter {

    public Conversation of(CreateConversationRequest createConversationRequest) {
        return new Conversation()
                .setListOfUsers(createConversationRequest.getListOfUsers())
                .setTitle(createConversationRequest.getTitle())
                .setMessages(new ArrayList<>());
    }
}
