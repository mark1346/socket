package Chatting.common;


import java.io.Serializable;

public class Message implements Serializable {
    private String sender;
    private String contect;

    public Message(String sender, String contect) {
        this.sender = sender;
        this.contect = contect;
    }

    public String getSender() {
        return sender;
    }
    public String getContect() {
        return contect;
    }

    @Override
    public String toString() {
        return sender + " : " + contect;
    }
}
