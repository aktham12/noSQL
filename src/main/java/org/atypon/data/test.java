package org.atypon.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.atypon.data.collection.CollectionManager;
import org.atypon.data.collection.JsonCollection;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Paths;
import java.util.ArrayList;

public class test {
    public static void main(String[] args) throws IOException {
        JsonCollection jsonCollection = new JsonCollection("soso","databases");
        String json = "{\"_id\": \"1\",\"name\":\"aktham\",\"age\":\"25\"}";

        String json2 = "{\"_id\": \"2\",\"name\":\"khaled\",\"age\":\"21\"}";
        String json3 = "{\"_id\": \"3\",\"name\":\"صهيب\",\"age\":\"21\"}";
        jsonCollection.insert(json);
        ArrayList<String> nodes = new ArrayList<>();
        nodes.add(json2);
        nodes.add(json3);

        jsonCollection.insertMany(nodes);


        for(JsonNode node : jsonCollection.getNodesArray()) {
            if(node.get("name").asText().equals("aktham")) {
                ObjectNode o = (ObjectNode) node;
                o.put("name", "aktham2");
                System.out.println(node);
            }
        }
    }
    public void updateOne(String property, String propertyValue, String newJson) {

    }
}
