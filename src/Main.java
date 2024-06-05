import java.io.*;
import java.net.*;

public class Main {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Server is listening on port 8080");

            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    System.out.println("New client connected");

                    // 입력 스트림으로부터 데이터 읽기
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String inputLine;
                    StringBuilder requestBuilder = new StringBuilder();

                    while ((inputLine = in.readLine()) != null && !inputLine.isEmpty()) {
                        requestBuilder.append(inputLine).append("\n");
                    }

                    System.out.println("Received request:\n" + requestBuilder.toString());

                    // 출력 스트림으로 응답 전송
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    out.println("HTTP/1.1 200 OK");
                    out.println("Content-Type: text/plain");
                    out.println("Content-Length: 28");
                    out.println();
                    out.println("Response from socket server");

                } catch (IOException e) {
                    System.out.println("Server exception: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("Could not listen on port 8080");
            e.printStackTrace();
        }
    }
}
