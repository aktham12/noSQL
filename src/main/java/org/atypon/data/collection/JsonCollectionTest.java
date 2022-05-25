package org.atypon.data.collection;

import java.io.IOException;
import java.util.ArrayList;

public class JsonCollectionTest {
    public static void main(String[] args) throws IOException {
        JsonCollection jsonCollection = new JsonCollection("Temp","databases");
        String json = "{\"_id\": \"1\",\"name\":\"aktham\",\"age\":\"25\"}";

        String json2 = "{\"_id\": \"2\",\"name\":\"khaled\",\"age\":\"21\"}";
        String json3 = "{\"_id\": \"3\",\"name\":\"Suhiab\",\"age\":\"21\"}";

        jsonCollection.insert(json);

        ArrayList<String> nodes = new ArrayList<>();
        nodes.add(json2);
        nodes.add(json3);
        jsonCollection.insertMany(nodes);
        System.out.println(jsonCollection.getNodesArray());
        System.out.println(  jsonCollection.find("_id", "1"));


    }
}
