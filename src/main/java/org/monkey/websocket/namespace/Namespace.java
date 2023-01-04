package org.monkey.websocket.namespace;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.monkey.filter.NamespaceFilter;
import org.monkey.filter.impl.DiscoveryFilter;
import org.monkey.filter.impl.MessageFilter;

public enum Namespace {
    DISCOVERY (new DiscoveryFilter()),
    MESSAGE (new MessageFilter()),
    LOGOUT (new LogoutFilter());

    private final NamespaceFilter namespaceFilter;

    Namespace(NamespaceFilter namespaceFilter) {
        this.namespaceFilter = namespaceFilter;
    }

    @SneakyThrows
    public static NamespaceFilter get(String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(message);

        switch(jsonNode.get("namespace").asText()) {
            case "DISCOVERY":
                return new DiscoveryFilter();
            case "MESSAGE":
                return new MessageFilter();
            case "LOGOUT":
                return new LogoutFilter();
            default:
                return null;
        }
    }
}
