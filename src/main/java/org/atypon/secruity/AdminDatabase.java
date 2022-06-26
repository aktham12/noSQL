package org.atypon.secruity;

import com.fasterxml.jackson.databind.JsonNode;
import org.atypon.cache.Cache;
import org.atypon.cache.LRUCache;
import org.atypon.data.collection.Database;
import org.atypon.data.collection.JsonCollection;
import org.atypon.data.collection.JsonDocument;
import org.atypon.node.LoadBalancer;
import org.atypon.node.NodeServer;
import org.atypon.services.CRUDService;
import org.atypon.services.CollectionService;
import org.atypon.services.DatabaseService;
import org.atypon.services.DocumentService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;


public class AdminDatabase implements DatabaseFacade {
    private final CRUDService crudService;
    public AdminDatabase() {
        this.crudService = setUpCRUD().build();
    }

    @Override
    public Optional<Database> createDatabase(String databaseName) {
         Optional<Database> database = Optional.of(DatabaseService.addDatabase(new Database(databaseName)));
         return database;
    }

    @Override
    public void deleteDatabase(String databaseName) {
        DatabaseService.deleteDatabase(databaseName);

    }

    @Override
    public Optional<Database> getDatabase(String databaseName) {
        return DatabaseService.getDatabase(databaseName);
    }

    @Override
    public Optional<ArrayList<Database>> loadDatabases() {
        return DatabaseService.getDatabaseList().map(ArrayList::new);
    }

    @Override
    public Optional<JsonCollection> createCollection(String databaseName, String collectionName) {
        Optional<JsonCollection> collection = CollectionService.addCollection(databaseName, new JsonCollection(collectionName));
        return collection;

    }

    @Override
    public void deleteCollection(String databaseName, String collectionName) {
        CollectionService.deleteCollection(databaseName, collectionName);

    }

    @Override
    public Optional<JsonCollection> getCollection(String databaseName, String collectionName) {
        return Optional.of(DatabaseService
                .getDatabase(databaseName)
                .filter(db -> db.getCollection(collectionName) != null)
                .map(db -> db.getCollection(collectionName)).get()
        );

    }

    @Override
    public Optional<JsonDocument> addDocument(String databaseName, String collectionName, String documentName) {
        return DocumentService.addDocument(databaseName, collectionName, new JsonDocument(documentName));

    }

    @Override
    public void deleteDocument(String databaseName, String collectionName, String documentName) {
        DocumentService.deleteDocument(databaseName, collectionName, documentName);
    }

    @Override
    public Optional<JsonDocument> getDocument(String databaseName, String collectionName, String documentName) {
        return DatabaseService.getDatabase(databaseName)
                .filter(db -> db.getCollection(collectionName)
                        .getDocument(documentName) != null)
                .map(db -> db.getCollection(collectionName)
                        .getDocument(documentName)
                );
    }

    @Override
    public Optional<ArrayList<JsonDocument>> getDocuments(String databaseName, String collectionName) {
        return Optional.of((ArrayList<JsonDocument>)
                DatabaseService.getDatabase(databaseName)
                        .filter(db -> db.getCollection(collectionName) != null)
                        .map(db -> db.getCollection(collectionName).getDocuments().values()).get());
    }

    @Override
    public void useDatabase(String databaseName) {
        crudService.setCurrentDatabase(databaseName);
    }

    @Override
    public void useCollection(String collectionName) {
        crudService.setCurrentCollection(collectionName);

    }

    @Override
    public void useDocument(String databaseName) {
        crudService.setCurrentDocument(databaseName);

    }

    @Override
    public void insert(String json) {
        crudService.insert(json);

    }

    @Override
    public void insertMany(ArrayList<String> jsons) {
        crudService.insertMany(jsons);
    }

    @Override
    public void updateOne(String property, String propertyValue, String newValue) {
        crudService.updateOne(property, propertyValue, newValue);
    }

    @Override
    public void updateMany(String property, String propertyValue, String newValue) {
        crudService.updateMany(property, propertyValue, newValue);
    }

    @Override
    public void deleteOne(String property, String propertyValue) {
        crudService.deleteOne(property, propertyValue);
}

    @Override
    public void deleteMany(String property, String propertyValue) {
        crudService.deleteMany(property, propertyValue);

    }

    @Override
    public void makeIndexOn(String property) {
        CollectionService.makeIndexOn(
                crudService.getCurrentDatabase().getDatabaseName(),
                crudService.getCurrentCollection().getName(),
                property
        );
    }

    @Override
    public ArrayList<JsonNode> find(String property, String propertyValue) {
        return null;
    }

    @Override
    public String toString() {
        return "You are an admin";
    }

    private CRUDService.CRUDServiceBuilder setUpCRUD() {
        CRUDService.CRUDServiceBuilder crudServiceBuilder = new CRUDService.CRUDServiceBuilder();
        crudServiceBuilder.currentDatabase("logins");
        crudServiceBuilder.currentCollection("users");
        crudServiceBuilder.currentDocument("logins.json");
        return crudServiceBuilder;
    }

    public String getCurrentDatabaseName() {
        return crudService.getCurrentDatabase().getDatabaseName();
    }



}
