package org.monkey.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.java_websocket.WebSocket;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ActiveUser {
    private String email;
    private String name;
    private String surname;
    private WebSocket webSocket;
}
