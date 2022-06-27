package org.atypon.services;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import org.atypon.concurrency.LocksManager;
import org.atypon.data.collection.Database;
import org.atypon.data.collection.JsonCollection;
import org.atypon.data.collection.JsonDocument;
import org.atypon.io.DirectoryCreator;
import org.atypon.io.DirectoryRemover;
import org.atypon.io.FileWriterReader;

import java.io.IOException;
import java.util.ArrayList;

@Getter
public class CRUDService {

    private Database currentDatabase;
    private JsonCollection currentCollection;
    private JsonDocument currentDocument;


    public CRUDService(CRUDServiceBuilder crudServiceBulider) {
        currentDatabase = crudServiceBulider.getCurrentDatabase();
        currentCollection = crudServiceBulider.getCurrentCollection();
        currentDocument = crudServiceBulider.getCurrentDocument();
    }

    public void insert(String json) {
        try {
            LocksManager.getInstance().getLock("CRUDLock").writeLock().lock();
            currentDocument.insert(json);
            FileWriterReader.getInstance().writeJson(currentDatabase.getDatabaseName() + "/" + currentCollection.getName() + "/" + currentDocument.getDocumentName(), currentDocument.getNodesArray());
            updateIndexer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            LocksManager.getInstance().getLock("CRUDLock").writeLock().unlock();
        }
    }

    public void insertMany(ArrayList<String> jsons) {
        try {
            LocksManager.getInstance().getLock("CRUDLock").writeLock().lock();
            currentDocument.insertMany(jsons);
            FileWriterReader.getInstance().writeJson(currentDatabase.getDatabaseName() + "/" + currentCollection.getName() + "/" + currentDocument.getDocumentName(), currentDocument.getNodesArray());
            updateIndexer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            LocksManager.getInstance().getLock("CRUDLock").writeLock().unlock();
        }
    }

    public void updateOne(String property, String propertyValue, String newValue) {
        try {
            LocksManager.getInstance().getLock("CRUDLock").writeLock().lock();
            currentDocument.updateOne(property, propertyValue, newValue);
            FileWriterReader.getInstance().writeJson(currentDatabase.getDatabaseName() + "/" + currentCollection.getName() + "/" + currentDocument.getDocumentName(), currentDocument.getNodesArray());
            updateIndexer();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            LocksManager.getInstance().getLock("CRUDLock").writeLock().unlock();
        }
    }

    public void updateMany(String property, String propertyValue, String newValue) {
        try {
            LocksManager.getInstance().getLock("CRUDLock").writeLock().lock();
            currentDocument.updateMany(property, propertyValue, newValue);
            FileWriterReader.getInstance().writeJson(currentDatabase.getDatabaseName() + "/" + currentCollection.getName() + "/" + currentDocument.getDocumentName(), currentDocument.getNodesArray());
            updateIndexer();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            LocksManager.getInstance().getLock("CRUDLock").writeLock().unlock();
        }
    }

    public void deleteOne(String property, String propertyValue) {
        try {
            LocksManager.getInstance().getLock("CRUDLock").writeLock().lock();
            currentDocument.delete(property, propertyValue);
            FileWriterReader.getInstance().writeJson(currentDatabase.getDatabaseName() + "/" + currentCollection.getName() + "/" + currentDocument.getDocumentName(), currentDocument.getNodesArray());

            updateIndexer();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            LocksManager.getInstance().getLock("CRUDLock").writeLock().unlock();
        }
    }

    public void deleteMany(String property, String propertyValue) {
        try {
            LocksManager.getInstance().getLock("CRUDLock").writeLock().lock();
            currentDocument.deleteMany(property, propertyValue);
            FileWriterReader.getInstance().writeJson(currentDatabase.getDatabaseName() + "/" + currentCollection.getName() + "/" + currentDocument.getDocumentName(), currentDocument.getNodesArray());

            updateIndexer();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            LocksManager.getInstance().getLock("CRUDLock").writeLock().unlock();
        }

    }

    public ArrayList<JsonNode> find(String property, String propertyValue) {
        return currentDocument.find(property, propertyValue);
    }




    public void setCurrentDatabase(String currentDatabase) {
        DatabaseService.getDatabase(currentDatabase).ifPresent(database -> this.currentDatabase = database);
    }

    public void setCurrentCollection(String currentCollection) {
        this.currentCollection = currentDatabase.getCollection(currentCollection);
    }

    public void setCurrentDocument(String currentDocument) {
        this.currentDocument = currentCollection.getDocument(currentDocument);
    }

    private void updateIndexer() throws IOException {
        FileWriterReader.getInstance().write(currentDocument.getIndexer(),
                DirectoryCreator.getInstance().getMasterDir() +
                        "/" +
                        currentDatabase.getDatabaseName() +
                        "/" +
                        currentCollection.getName() +"/"
                        + "indexer.ser"
        );
    }

    @Getter
    public static class CRUDServiceBuilder {
        private Database currentDatabase;
        private JsonCollection currentCollection;
        private JsonDocument currentDocument;

        public CRUDServiceBuilder currentDatabase(String databaseName) {
            DatabaseService.getDatabase(databaseName).ifPresent(database -> this.currentDatabase = database);
            return this;
        }

        public CRUDServiceBuilder currentCollection(String collectionName) {
            DatabaseService.getDatabase(currentDatabase.getDatabaseName()).filter(
                    database -> database.getCollection(collectionName) != null
            ).map(
                    database -> this.currentCollection = database.getCollection(collectionName)
            );


            return this;
        }

        public CRUDServiceBuilder currentDocument(String documentName) {
            this.currentDocument = currentCollection.getDocument(documentName);
            return this;
        }

        public CRUDService build() {
            return new CRUDService(this);
        }
    }


}




