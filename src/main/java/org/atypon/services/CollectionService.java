package org.atypon.services;

import org.atypon.concurrency.LocksManager;
import org.atypon.data.collection.JsonCollection;
import org.atypon.io.DirectoryCreator;
import org.atypon.io.DirectoryRemover;

import java.util.Optional;

public class CollectionService {
    private CollectionService() {
    }

    public static Optional<JsonCollection> addCollection(String databaseName, JsonCollection collection) {
        try {
            LocksManager.getInstance().getLock("CollectionLock").writeLock().lock();
            DatabaseService.getDatabase(databaseName).ifPresent(database -> database.addCollection(collection.getName()));
            DirectoryCreator.getInstance()
                    .createDirectory(databaseName
                            + "/"
                            + collection.getName()
                    );
            return Optional.of(collection);
        }finally {
            LocksManager.getInstance().getLock("CollectionLock").writeLock().unlock();
        }

    }

    public static void deleteCollection(String databaseName, String collectionName) {
        try {
            LocksManager.getInstance().getLock("CollectionLock").writeLock().lock();

        DatabaseService.getDatabase(databaseName).ifPresent(database -> database.deleteCollection(collectionName));
        DirectoryRemover.getInstance()
                .deleteDirectory(databaseName
                        + "/"
                        + collectionName
                );
    }
    finally {
        LocksManager.getInstance().getLock("CollectionLock").writeLock().unlock();
        }
    }

    public static void makeIndexOn(String databaseName,String collectionName,String property) {
        try {
            LocksManager.getInstance().getLock("CRUDLock").writeLock().lock();
            DatabaseService.getDatabase(databaseName).ifPresent(
                    database -> database.getCollection(collectionName).getDocuments().forEach(
                            (s, jsonDocument) -> jsonDocument.makeIndexOn(property)
                    )
            );

        } finally {
            LocksManager.getInstance().getLock("CRUDLock").writeLock().unlock();

        }
    }

}
