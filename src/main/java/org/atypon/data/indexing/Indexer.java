package org.atypon.data.indexing;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;

public interface Indexer {
    void makeIndexOn(String property, ArrayList<JsonNode> jsonNode);

    ArrayList<JsonNode> get(String property, String exactValue);

    void addToIndexed(String property, JsonNode node);

    String[] getAllPropertyIndexed();

    void addToAllIndexed(JsonNode node);

    void deleteFromIndexed(String property, JsonNode node);

    void deleteFromAllIndexed(JsonNode node);

    void deleteSpecificKey(String property, String key);


}
