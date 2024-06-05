import java.io.*;
import java.net.*;

public class ChatClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) { // 소켓 객체 생성하여 서버에 연결

            //서버와 통신을 위한 입력, 출력 스트림 설정
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // 키보드 입력을 위한 BufferedReader 객체 생성
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

            // 서버로부터 메시지 읽기 스레드
            new Thread(new IncomingReader(in)).start();

            // 클라이언트 이름 전송
            System.out.print("Enter your name: ");
            String name = keyboard.readLine();
            out.println(name);

            // 키보드로부터 메시지 읽고 서버로 전송
            String message;
            while ((message = keyboard.readLine()) != null) {
                out.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // 서버로부터 수신된 메시지 처리
    private static class IncomingReader implements Runnable {

        // 서버로부터 메시지를 읽기 위한 BufferedReader 객체
        private BufferedReader in;

        public IncomingReader(BufferedReader in) {
            this.in = in;
        }

        public void run() { //읽은 메세지를 출력
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
