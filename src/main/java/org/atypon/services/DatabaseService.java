package org.atypon.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.atypon.concurrency.LocksManager;
import org.atypon.data.collection.Database;
import org.atypon.data.collection.JsonCollection;
import org.atypon.data.collection.JsonDocument;
import org.atypon.io.DirectoryCreator;
import org.atypon.io.DirectoryRemover;
import org.atypon.io.FileLoader;
import org.atypon.io.FileWriterReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class DatabaseService {

    private static final List<Database> DATABASE_LIST = loadDatabases();


    public static Database addDatabase(Database database) {
        try {
            LocksManager.getInstance().getLock("DatabaseLock").writeLock().lock();
            DATABASE_LIST.add(database);
            DirectoryCreator.getInstance().createDirectory(database.getDatabaseName());
            return database;
        }finally {
            LocksManager.getInstance().getLock("DatabaseLock").writeLock().unlock();

        }

    }

    public static void deleteDatabase(String databaseName) {
        try {
            LocksManager.getInstance().getLock("DatabaseLock").writeLock().lock();
            DATABASE_LIST.removeIf(database -> database.getDatabaseName().equals(databaseName));
            DirectoryRemover.getInstance().deleteDirectory(databaseName);
        } finally {
            LocksManager.getInstance().getLock("DatabaseLock").writeLock().unlock();

        }


    }

    public  static Optional<Database> getDatabase(String databaseName) {
        try {
            LocksManager.getInstance().getLock("DatabaseLock").writeLock().lock();
            Optional<List<Database>> temp = getDatabaseList();
            if (temp.isPresent()) {
                return getDatabaseList().get().stream().filter(database -> database.getDatabaseName().equals(databaseName)).findFirst();
            }
        }
        finally{
                LocksManager.getInstance().getLock("DatabaseLock").writeLock().unlock();

            }
        return Optional.empty();
    }


    public static Optional<List<Database>> getDatabaseList() {
        return Optional.of(loadDatabases());
    }

    private static List<Database> loadDatabases() {
        File mainFile = DirectoryCreator.getInstance().getMasterDir();
        List<Database> databases = new ArrayList<>();
        FileLoader.getInstance().loadDirectories(mainFile).forEach(directory -> {
            Database database = new Database(directory.getName());
            databases.add(database);
        });

        for (int i = 0; i < databases.size(); i++) {
            int j = i;
            File temp = new File(mainFile.getPath() + "/" + databases.get(i).getDatabaseName());
            FileLoader.getInstance().loadDirectories(temp).forEach(
                    collection -> {
                        databases.get(j)
                                .getCollectionGroup()
                                .put(collection.getName(),
                                        new JsonCollection(collection.getName())
                                );
                    }
            );

            for (JsonCollection collection : databases.get(i).
                    getCollectionGroup().
                    values()) {
                AtomicReference<JsonDocument> tempDocument = new AtomicReference<>();
                FileLoader.getInstance().loadFiles(
                        new File(mainFile.getPath()
                                + "/"
                                + databases.get(i).getDatabaseName()
                                + "/"
                                + collection.getName())
                ).forEach(
                        file1 -> {
                            ArrayNode nodes = new ObjectMapper().createArrayNode();
                            try {
                                JsonNode node = new ObjectMapper().readTree(file1);
                                if (!node.isEmpty()) {
                                    nodes.add(node);
                                }
                                if (!nodes.isEmpty()) {
                                    String json;
                                    json = nodes.toString().replace("[", "").replace("]", "");
                                    String json3 = "[" + json + "]";
                                    nodes = new ObjectMapper().readValue(json3, ArrayNode.class);
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            tempDocument.set(databases.get(j)
                                    .getCollection(collection.getName())
                                    .addDocument(new JsonDocument(file1.getName())));

                            if (!nodes.isEmpty()) {
                                tempDocument.get().setNodesArray(nodes);
                                try {
                                    File file =new File(databases.get(j).getDatabaseName() +"/" + collection.getName() + "/" + "indexer.out");
                                    if(file.exists()) {
                                        tempDocument.get().getIndexer().setIndexed(FileWriterReader.getInstance().read("indexed.out"));
                                    }
                                } catch (IOException | ClassNotFoundException e) {
                                    throw new RuntimeException(e);
                                }

                            }
                            tempDocument.get().getIndexer().makeIndexOn("_id", tempDocument.get().getNodesArray());
                        }

                );


            }
        }
        return databases;
    }
}



