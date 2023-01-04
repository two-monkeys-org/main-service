package org.monkey.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Document
public class Conversation {
    @Id
    private String UUID;
    private String title;
    private List<ConversationParticipant> listOfUsers;
    private List<Message> messages;
}
