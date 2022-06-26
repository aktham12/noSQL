package org.atypon.node;

import org.atypon.io.SocketFileManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import java.util.concurrent.ConcurrentLinkedQueue;

public class NodeServer{

    private final Queue<Socket> connections;
    private static PrintWriter writer;

    public NodeServer() {
        connections = new ConcurrentLinkedQueue<>();
    }

    public void connect(ServerSocket serverSocket) throws IOException, InterruptedException {
        System.out.println("new connection");
        Socket socket = serverSocket.accept();
        this.subscribe(socket);
        this.broadcast(socket);
    }



    public void subscribe(Socket node) {
        connections.add(node);
    }

    public void unSubscribe(Socket node) {
        connections.remove(node);
    }

    public void broadcast(Socket node)  {
        SocketFileManager socketFileManager =SocketFileManager.create(node);
        try {
            writer = new PrintWriter(node.getOutputStream());
            writer.println("sending databases");
            writer.flush();
            socketFileManager.sendFile("databases");
            Thread.sleep(1000);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendDatabase(String databaseName) {
        for(Socket node : connections) {
            SocketFileManager fileManager = SocketFileManager.create(node);
            try {
                writer = new PrintWriter(node.getOutputStream());
                writer.println(databaseName);
                writer.flush();
                fileManager.sendFile("databases/"+databaseName);
                Thread.sleep(100);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void runServer() throws IOException {
        ServerSocket masterNode = new ServerSocket(9097);

        Thread t1 = new Thread(() -> {
            try {
                while (true) {
                    connect(masterNode);
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        t1.start();
    }

}
