package Chatting.server;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class ChatServer {
    private static final int PORT = 12345;
    private static ConcurrentHashMap<String, ClientHandler> clients = new ConcurrentHashMap<>(); // 동시성 처리

    public static void main(String[] args) {
        System.out.println("Chat server started on port " + PORT);
        try(ServerSocket serverSocket = new ServerSocket(PORT)) {
            while(true) {
                try {
                    Socket socket = serverSocket.accept();
                    ClientHandler handler = new ClientHandler(socket, clients);
                    handler.start();
                } catch (IOException e) {
                    System.out.println("Error accepting client connection: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("Could not start server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
