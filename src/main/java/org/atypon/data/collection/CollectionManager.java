package org.atypon.data.collection;

import BPlusTree.BTree;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.DataInput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class CollectionManager {
    ObjectMapper objectMapper;

    private final ArrayList<JsonCollection> jsonCollections;

    private final BTree<String, BTree<String,ArrayList<JsonNode>>> indexes;

    public CollectionManager() {
        objectMapper = new ObjectMapper();
        this.jsonCollections = new ArrayList<>();
        indexes = new BTree<>();
    }



    public Optional<JsonCollection> createCollection(String name,String path) {

        try {
            JsonCollection jsonCollection = new JsonCollection(name,path);
            jsonCollections.add(jsonCollection);
            return Optional.of(jsonCollection);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void addCollection(JsonCollection jsonCollection) throws IOException {
        jsonCollections.add(jsonCollection);
    }



    public JsonCollection findById(String id) {
        for(JsonCollection jsonCollection : jsonCollections) {
            if(Objects.equals(jsonCollection.getId(), id)) {
                return jsonCollection;
            }
        }
        return null;
    }

    public JsonCollection findByName(String name) {
        for(JsonCollection jsonCollection : jsonCollections) {
            if(Objects.equals(jsonCollection.getName(), name)) {
                return jsonCollection;
            }
        }
        return null;
    }


    public void deleteById(String id) {
        for(JsonCollection jsonCollection : jsonCollections) {
            if(Objects.equals(jsonCollection.getName(), id)) {
                 jsonCollection.deleteCollection();
            }
        }
    }

    public void deleteByName(String name) {
        for(JsonCollection jsonCollection : jsonCollections) {
            if(Objects.equals(jsonCollection.getName(), name)) {
                 jsonCollection.deleteCollection();
            }
        }

    }

    public void deleteAll() {
        for(JsonCollection jsonCollection : jsonCollections) {
            jsonCollection.deleteCollection();
        }
    }


    public ArrayList<JsonNode> readById(String id) throws IOException {
        BTree<String,ArrayList<JsonNode>> temp=indexes.search("id");
        ArrayList<JsonNode> jsonNodes = temp.search("\"1\"");
        return jsonNodes;

    }

    public void readByProperty(String property) {
        if(indexes.search(property) != null) {
            indexes.search(property);
        }
        else
        {
            //readNonIndexingProperty(property);
        }

    }





private ArrayList<JsonNode> getNodes() throws IOException {
        ArrayList<JsonNode> nodes = new ArrayList<>();
        for(JsonCollection collection : jsonCollections) {
            nodes.add(collection.readCollection());
        }
        return nodes;
}
/*
private void readNonIndexingProperty(String property) {
        for(JsonCollection collection: jsonCollections) {
            try {
                JsonNode node = collection.readCollection();
                if (node.has(property)) {



                }
            }catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

}

 */












}
