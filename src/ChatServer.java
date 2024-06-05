import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class ChatServer {
    private static final int PORT = 12345;
    private static ConcurrentHashMap<String, PrintWriter> clientWriter = new ConcurrentHashMap<>(); // 동시성 처리

    public static void main(String[] args) {
        System.out.println("Chat server started on port " + PORT);
        try(ServerSocket serverSocket = new ServerSocket(PORT)) {
            while(true) {
                new ClientHandler(serverSocket.accept()).start(); // 새 클라이언트가 접속할 때마다 새로운 스레드 생성
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String clientName;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try{
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // 클라이언트 이름 받기
                out.println("Enter your name: ");
                clientName = in.readLine();
                clientWriter.put(clientName, out); //클라이언트 이름과 출력 스트림을 해쉬맵에 저장

                // 메세지 브로드캐스트
                String message;
                while((message = in.readLine()) != null) { // 클라이언트로부터 메세지 읽기
                    System.out.println("Received message from " + clientName + ": " + message);
                    for(PrintWriter writer : clientWriter.values()) { // 모든 클라이언트에게 메세지 전송
                        writer.println(clientName + ": " + message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(clientName != null) {
                    clientWriter.remove(clientName); // 클라이언트가 연결 종료하면 이름을 목록에서 제거
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
