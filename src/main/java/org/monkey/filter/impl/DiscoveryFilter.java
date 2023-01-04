package org.monkey.filter.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.monkey.entity.ActiveUser;
import org.monkey.filter.NamespaceFilter;

public class DiscoveryFilter implements NamespaceFilter {
    private final static String namespace = "DISCOVERY";

    @Override
    @SneakyThrows
    public void process(String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(message);

        ActiveUser user = objectMapper.readValue(jsonNode.get("payload").asText(), ActiveUser.class);

//        return user;
    }
}
