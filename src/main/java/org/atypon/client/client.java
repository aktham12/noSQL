package org.atypon.client;

import org.apache.commons.io.FileUtils;
import org.atypon.io.SocketFileManager;
import org.atypon.io.ZipManager;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8084);
        Scanner scanner = new Scanner(System.in);
        BufferedReader bufferReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String input;
        System.out.println(bufferReader.readLine());
        input = scanner.nextLine();
        send(input, socket);
        System.out.println(bufferReader.readLine());
        System.out.println(bufferReader.readLine());
        SocketFileManager socketFileManager = SocketFileManager.create(socket);

        while (true) {
            System.out.println(bufferReader.readLine());
            input = scanner.nextLine();
            send(input, socket);
            switch (input) {
                case "exit":
                    socket.close();
                    return;
                case "createDatabase":
                case "deleteDatabase":
                case "createCollection":
                case "deleteCollection":
                case "addDocument":
                case "deleteDocument":
                case "useDatabase":
                case "useCollection":
                case "useDocument":
                case "insert":
                case "updateOne":
                case "updateMany":
                case "deleteOne":
                case "deleteMany":
                    System.out.println(bufferReader.readLine());
                    input = scanner.nextLine();
                    send(input, socket);
                    break;
                case "exportDatabase":
                    System.out.println(bufferReader.readLine());
                    input = scanner.nextLine();
                    send(input, socket);
                    socketFileManager.receiveFile(input, "new databases");
                    break;
                case "importDatabase":
                    System.out.println(bufferReader.readLine());
                    input = scanner.nextLine();
                    send(input, socket);
                    input = scanner.nextLine();
                    socketFileManager.sendFile(input);
                    break;
                case "exportDatabases":
                    System.out.println(bufferReader.readLine());
                    input = scanner.nextLine();
                    send(input, socket);
                    socketFileManager.receiveFile(input, "all databases");
                    break;


            }


        }
    }

    public static void send(String message, Socket socket) throws IOException {
        PrintWriter pr = new PrintWriter(socket.getOutputStream());
        pr.println(message);
        pr.flush();
    }




}

