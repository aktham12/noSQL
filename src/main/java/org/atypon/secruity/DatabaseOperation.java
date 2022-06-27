package org.atypon.secruity;

import com.fasterxml.jackson.databind.JsonNode;
import org.atypon.data.collection.Database;
import org.atypon.data.collection.JsonCollection;
import org.atypon.data.collection.JsonDocument;

import java.util.ArrayList;
import java.util.Optional;

public interface DatabaseOperation {

    Optional<Database> createDatabase(String databaseName);

    void deleteDatabase(String databaseName);

    Optional<Database> getDatabase(String databaseName);

    Optional<ArrayList<Database>> loadDatabases();

    Optional<JsonCollection> createCollection(String databaseName, String collectionName);

    void deleteCollection(String databaseName, String collectionName);
    Optional<JsonCollection> getCollection(String databaseName, String collectionName);

    Optional<JsonDocument> addDocument(String databaseName, String collectionName, String documentName);

    void deleteDocument(String databaseName, String collectionName, String documentName);

    Optional<JsonDocument> getDocument(String databaseName, String collectionName, String documentName);

    Optional<ArrayList<JsonDocument>> getDocuments(String databaseName, String collectionName);

    void useDatabase(String databaseName);

    void useCollection(String collectionName);

    void useDocument(String databaseName);
    void insert(String json);

    void insertMany(ArrayList<String> jsons);

    void updateOne(String property, String propertyValue, String newValue);

    void updateMany(String property, String propertyValue, String newValue);

    void deleteOne(String property, String propertyValue);

    void deleteMany(String property, String propertyValue);
    void makeIndexOn(String property);

    ArrayList<JsonNode> find(String property, String propertyValue);
     String getCurrentDatabaseName();

     void scaleHorizontal(int n);

}


















