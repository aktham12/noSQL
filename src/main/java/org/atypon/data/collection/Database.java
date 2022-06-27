package org.atypon.data.collection;

import java.util.HashMap;

public class Database {
    private final String name;
    private final HashMap<String, JsonCollection> collections;

    public Database() {
        this.name = "default";
        collections = new HashMap<>();
    }

    public Database(String name) {
        this.name = name;
        collections = new HashMap<>();
    }

    public void addCollection(String name) {
        collections.put(name,
                new JsonCollection(name));
    }

    public JsonCollection getCollection(String name) {
        return collections.get(name);
    }

    public void deleteCollection(String name) {
        collections.remove(name);
    }

    public String getDatabaseName() {
        return name;
    }

    public HashMap<String, JsonCollection> getCollectionGroup() {
        return collections;
    }


}
