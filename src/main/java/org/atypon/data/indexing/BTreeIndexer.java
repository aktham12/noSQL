package org.atypon.data.indexing;

import BPlusTree.BTree;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

@Getter
public class BTreeIndexer implements Indexer {
    private final BTree<String, BTree<String, ArrayList<JsonNode>>> index;

    public BTreeIndexer() {
        index = new BTree<>();
    }

    @Override
    public void makeIndexOn(String property, ArrayList<JsonNode> jsonNode) {
        if (index.search(property) == null)
            index.insert(property, new BTree<>());
        for (JsonNode node : jsonNode) {
            addToIndexed(property, node);
        }
    }

    @Override
    public void addToIndexed(String property, JsonNode node) {
        String nodeValue = node.get(property).asText();
        BTree<String, ArrayList<JsonNode>> temp = index.search(property);
        if (temp.search(nodeValue) == null) {
            temp.insert(nodeValue, new ArrayList<>());
        }
        temp.search(nodeValue).add(node);
    }

    @Override
    public void addToAllIndexed(JsonNode node) {
        for (String property : getAllPropertyIndexed()) {
            this.addToIndexed(property, node);
        }

    }


    @Override
    public ArrayList<JsonNode> get(String property, String exactValue) {
        if (index.search(property) == null)
            return null;
        return new ArrayList<>(index.search(property).search(exactValue));
    }

    @Override
    public void deleteFromIndexed(String key, JsonNode node) {
        String value = node.get(key).asText();
        BTree<String, ArrayList<JsonNode>> temp = index.search(key);
        if (temp.search(value) != null) {
            temp.search(value).remove(node);
        }
    }

    @Override
    public void deleteSpecificKey(String property, String key) {
        if (index.search(property) == null)
            return;
        index.search(property).delete(key);
    }

    @Override
    public void deleteFromAllIndexed(JsonNode node) {
        for (String property : getAllPropertyIndexed()) {
            this.deleteFromIndexed(property, node);
        }
    }


    @Override
    public String[] getAllPropertyIndexed() {
        return Arrays.stream(this.index.getKeys()).filter(Objects::nonNull).toArray(String[]::new);
    }

    @Override
    public boolean has(String key) {
        return index.search(key) != null;
    }


}
