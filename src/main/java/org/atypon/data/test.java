package org.atypon.data;

import org.atypon.data.collection.CollectionManager;
import org.atypon.data.collection.JsonCollection;

import java.io.FileNotFoundException;
import java.io.IOException;

public class test {
    public static void main(String[] args) throws IOException {
        JsonCollection jsonCollection = new JsonCollection("aktham","databases","1");
        String json = "{\"name\":\"aktham\",\"age\":\"25\"}";
        jsonCollection.insert(json);
        json = "{\"name\":\"khaled\",\"age\":\"21\"}";
        JsonCollection jsonCollection1 = new JsonCollection("khaled","databases","1");
        jsonCollection1.insert(json);

        CollectionManager collectionManager = new CollectionManager();
        collectionManager.addCollection(jsonCollection);

        System.out.println(collectionManager.readById("1"));









    }
}
