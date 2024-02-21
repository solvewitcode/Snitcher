package com.example.snitcher.Model;

public class MessageModel {
    String message;
    String senderId;
    long timeStamp;
    String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public MessageModel(String message, String senderId, long timeStamp, String type) {
        this.message = message;
        this.senderId = senderId;
        this.timeStamp = timeStamp;
        this.type = type;
    }

    public MessageModel() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
