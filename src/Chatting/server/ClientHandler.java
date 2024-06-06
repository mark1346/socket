package Chatting.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ClientHandler extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String clientName;
    private ConcurrentHashMap<String, ClientHandler> clients;

    public ClientHandler(Socket socket, ConcurrentHashMap<String, ClientHandler> clients) {
        this.socket = socket;
        this.clients = clients;
    }

    public void run() {
        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // 클라이언트 이름 받기
            out.println("Enter your name: ");
            clientName = in.readLine();
            clients.put(clientName, this); //클라이언트 이름과 출력 스트림을 해쉬맵에 저장

            // 메세지 브로드캐스트
            String message;
            while((message = in.readLine()) != null) { // 클라이언트로부터 메세지 읽기
                System.out.println("Received message from " + clientName + ": " + message);
                broadcaseMessage(clientName, message); // 모든 클라이언트에게 메세지 전송
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(clientName != null) {
                clients.remove(clientName); // 클라이언트가 연결 종료하면 이름을 목록에서 제거
            }
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void broadcaseMessage(String sender, String message) {
        for(ClientHandler client : clients.values()) {
            client.out.println(sender + ": " + message);
        }
    }
}