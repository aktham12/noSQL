package org.atypon.secruity;

import com.fasterxml.jackson.databind.JsonNode;
import org.atypon.data.collection.Database;
import org.atypon.data.collection.JsonCollection;
import org.atypon.data.collection.JsonDocument;
import org.atypon.services.CRUDService;
import org.atypon.services.CollectionService;

import java.util.ArrayList;
import java.util.Optional;

public class UserDatabase implements DatabaseOperation {
    CRUDService crudService;


    public UserDatabase() {
        CRUDService.CRUDServiceBuilder crudServiceBuilder = new CRUDService.CRUDServiceBuilder();
        crudServiceBuilder.currentDatabase("logins");
        crudServiceBuilder.currentCollection("users");
        crudServiceBuilder.currentDocument("logins.json");
        crudService = crudServiceBuilder.build();
    }

    @Override
    public Optional<Database> createDatabase(String databaseName) {
        noAccess();
        return Optional.empty();
    }

    @Override
    public void deleteDatabase(String databaseName) {
        noAccess();

    }

    @Override
    public Optional<Database> getDatabase(String databaseName) {
        return Optional.empty();
    }

    @Override
    public Optional<ArrayList<Database>> loadDatabases() {
        noAccess();
        return Optional.empty();
    }

    @Override
    public Optional<JsonCollection> createCollection(String databaseName, String collectionName) {
        noAccess();
        return Optional.empty();
    }

    @Override
    public void deleteCollection(String databaseName, String collectionName) {
        noAccess();

    }

    @Override
    public Optional<JsonCollection> getCollection(String databaseName, String collectionName) {
        return Optional.empty();
    }

    @Override
    public Optional<JsonDocument> addDocument(String databaseName, String collectionName, String document) {
        noAccess();
        return Optional.empty();
    }

    @Override
    public void deleteDocument(String databaseName, String collectionName, String documentName) {
        noAccess();
    }

    @Override
    public Optional<JsonDocument> getDocument(String databaseName, String collectionName, String documentName) {
        return Optional.empty();
    }

    @Override
    public Optional<ArrayList<JsonDocument>> getDocuments(String databaseName, String collectionName) {
        return Optional.empty();
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
    public String getCurrentDatabaseName() {
        return null;
    }

    @Override
    public void scaleHorizontal(int n) {
        noAccess();
    }

    private void noAccess() {
        System.out.println("User doesn't have access to this command ");
    }

    @Override
    public String toString() {
        return "You are a user";
    }
}
