package org.atypon.server;


import com.fasterxml.jackson.databind.JsonNode;
import org.atypon.cache.Cache;
import org.atypon.cache.LRUCache;
import org.atypon.io.SocketFileManager;
import org.atypon.node.LoadBalancer;
import org.atypon.node.NodeServer;
import org.atypon.secruity.DatabaseFacade;
import org.atypon.secruity.DatabaseFactory;
import org.atypon.services.CRUDService;
import org.atypon.services.CollectionService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Server implements Runnable {
    private final Socket socket;
    private final BufferedReader bufferedReader;

    private DatabaseFacade databases;
    private final CRUDService crudService;

    private final SocketFileManager socketFileManager;
    private final Cache<String, JsonNode> cache;
    private final NodeServer readingNodesServer;

    public Server(Socket socket, NodeServer readingNodeServer) throws Exception {
        this.socket = socket;
        InputStreamReader inputStream = new InputStreamReader(socket.getInputStream());
        bufferedReader = new BufferedReader(inputStream);
        CRUDService.CRUDServiceBuilder builder = new CRUDService.CRUDServiceBuilder();
        builder.currentDatabase("logins");
        builder.currentCollection("users");
        builder.currentDocument("logins.json");
        crudService = builder.build();
        socketFileManager = SocketFileManager.create(this.socket);
        cache = new LRUCache<>(100);
        this.readingNodesServer = readingNodeServer;


    }

    @Override
    public void run() {
        System.out.println("New connection");
        try {
            String credits = enterCredits();
            databases = DatabaseFactory.getDatabase(testCredit(credits));
            while (!socket.isClosed()) {
                String message = bufferedReader.readLine();
                switch (message) {
                    case "exit":
                        socket.close();
                        return;
                    case "createDatabase":
                        String databaseName = bufferedReader.readLine();
                        databases.createDatabase(databaseName);
                        send("done", socket);
                        readingNodesServer.sendDatabase(databaseName);
                        break;
                    case "deleteDatabase":
                        send("Enter the name of the database:", socket);
                        String databaseName1 = bufferedReader.readLine();
                        databases.deleteDatabase(databaseName1);
                        readingNodesServer.sendDatabase(databaseName1 + "delete");
                        break;
                    case "createCollection":
                        String[] collectionInfo = bufferedReader.readLine().split(" ");
                        databases.createCollection(collectionInfo[0], collectionInfo[1]);
                        readingNodesServer.sendDatabase(collectionInfo[0]);
                        send("done", socket);

                        break;
                    case "deleteCollection":
                        send("Enter the name of the collection and the database:", socket);
                        String[] collectionInfo1 = bufferedReader.readLine().split(" ");
                        databases.deleteCollection(collectionInfo1[0], collectionInfo1[1]);
                        readingNodesServer.sendDatabase(collectionInfo1[0]);
                        send("done", socket);
                        break;
                    case "createDocument":
                        String[] collectionInfo2 = bufferedReader.readLine().split(" ");
                        databases.addDocument(collectionInfo2[0], collectionInfo2[1], collectionInfo2[2]);
                        readingNodesServer.sendDatabase(collectionInfo2[0]);
                        send("done", socket);


                        break;
                    case "deleteDocument":
                        String[] collectionInfo3 = bufferedReader.readLine().split(" ");
                        databases.deleteDocument(collectionInfo3[0], collectionInfo3[1], collectionInfo3[2]);
                        readingNodesServer.sendDatabase(collectionInfo3[0]);
                        send("done", socket);

                        break;
                    case "useDatabase":
                        String databaseName2 = bufferedReader.readLine();
                        databases.useDatabase(databaseName2);
                        send("done", socket);
                        break;
                    case "useCollection":
                        String collectionName = bufferedReader.readLine();
                        databases.useCollection(collectionName);
                        send("done", socket);

                        break;
                    case "useDocument":
                        String documentName = bufferedReader.readLine();
                        databases.useDocument(documentName);
                        send("done", socket);

                        break;
                    case "insert":
                        String json = bufferedReader.readLine();
                        databases.insert(json);
                        readingNodesServer.sendDatabase(databases.getCurrentDatabaseName());

                        send("done", socket);
                        break;
                    case "updateOne":
                        String[] json0 = bufferedReader.readLine().split(" ");
                        databases.updateOne(json0[0], json0[1], json0[2]);
                        readingNodesServer.sendDatabase(databases.getCurrentDatabaseName());
                        send("done", socket);

                        break;
                    case "updateMany":
                        String[] json1 = bufferedReader.readLine().split(" ");
                        databases.updateMany(json1[0], json1[1], json1[2]);
                        readingNodesServer.sendDatabase(databases.getCurrentDatabaseName());
                        send("done", socket);

                        break;
                    case "deleteOne":
                        String[] json2 = bufferedReader.readLine().split(" ");
                        databases.deleteOne(json2[0], json2[1]);
                        readingNodesServer.sendDatabase(databases.getCurrentDatabaseName());
                        send("done", socket);

                        break;
                    case "deleteMany":
                        String[] json3 = bufferedReader.readLine().split(" ");
                        databases.deleteMany(json3[0], json3[1]);
                        readingNodesServer.sendDatabase(databases.getCurrentDatabaseName());
                        send("done", socket);
                        break;
                    case "makeIndexOn":
                        String index = bufferedReader.readLine();
                        databases.makeIndexOn(index);
                        send("done",socket);
                        break;
                    case "exportDatabase":
                        String databaseNamed = bufferedReader.readLine();
                        socketFileManager.sendFile("databases/" + databaseNamed);
                        send("done", socket);
                        break;
                    case "exportDatabases":
                        socketFileManager.sendFile("databases");
                        send("done", socket);
                        break;
                    case "importDatabase":
                        String databaseName4 = bufferedReader.readLine();
                        socketFileManager.receiveFile(databaseName4, "databases");
                        send("done", socket);
                        break;
                    case "find":
                        send(String.valueOf(LoadBalancer.getRandomPort()), socket);
                        send("done", socket);
                        break;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    private String enterCredits() throws IOException {
        String info = bufferedReader.readLine();
        send("done", socket);
        return info;
    }


    private boolean testCredit(String creids) {
        String[] credits = creids.split(" ");
        System.out.println(credits[0]);
        ArrayList<JsonNode> list = crudService.find("username", credits[0]);
        if (list == null || list.size() == 0) {
            return false;
        } else return list.get(0).get("password").asText().equals(credits[1]);
    }


    public static void send(String message, Socket socket) throws IOException {
        PrintWriter pr = new PrintWriter(socket.getOutputStream());
        pr.println(message);
        pr.flush();
    }


}

public class MainServer {
    public static void main(String[] args) {

        NodeServer readingNodesServer = new NodeServer();
        try (ServerSocket serverSocket = new ServerSocket(8084)) {
            readingNodesServer.runServer();
            LoadBalancer.runNodes(Integer.parseInt(args[0]));

            ExecutorService executorService = Executors.newCachedThreadPool();
            while (true) {
                Socket socket = serverSocket.accept();
                executorService.submit(new Server(socket, readingNodesServer));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}





