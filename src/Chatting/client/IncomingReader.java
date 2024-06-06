package Chatting.client;

import java.io.BufferedReader;
import java.io.IOException;

// 서버로부터 수신된 메시지 처리
public class IncomingReader implements Runnable {

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