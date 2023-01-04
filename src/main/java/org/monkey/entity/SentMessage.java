package org.monkey.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SentMessage {
    private String NAMESPACE;
    private String from;
    private String text;
    private String uuid;
}
