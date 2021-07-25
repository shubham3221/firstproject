package com.example.s_tools.chatting.messageModel;

import androidx.annotation.Keep;

import com.google.gson.JsonObject;

@Keep
public class AllMessages {
    private String date_sent;
    private JsonObject message;
    private int sender_id;

    public AllMessages(String date_sent, JsonObject message, int sender_id) {
        this.date_sent=date_sent;
        this.message=message;
        this.sender_id=sender_id;
    }

    public int getSender_id() {
        return sender_id;
    }

    public void setSender_id(int sender_id) {
        this.sender_id=sender_id;
    }

    public String getDate_sent() {
        return date_sent;
    }

    public void setDate_sent(String date_sent) {
        this.date_sent=date_sent;
    }

    public JsonObject getMessage() {
        return message;
    }

    public void setMessage(JsonObject message) {
        this.message=message;
    }
}
