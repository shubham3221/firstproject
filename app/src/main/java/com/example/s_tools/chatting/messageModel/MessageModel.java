package com.example.s_tools.chatting.messageModel;

import androidx.annotation.Keep;

import java.util.List;


@Keep
public class MessageModel {
    private int id;
    private int message_id;
    private int last_sender_id;
    private String date;
    private int unread_count;
    private List<AllMessages> messages;

    public MessageModel(int id, int message_id, int last_sender_id, String date, int unread_count, List<AllMessages> messages) {
        this.id=id;
        this.message_id=message_id;
        this.last_sender_id=last_sender_id;
        this.date=date;
        this.unread_count=unread_count;
        this.messages=messages;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id=id;
    }

    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id=message_id;
    }

    public int getLast_sender_id() {
        return last_sender_id;
    }

    public void setLast_sender_id(int last_sender_id) {
        this.last_sender_id=last_sender_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date=date;
    }

    public int getUnread_count() {
        return unread_count;
    }

    public void setUnread_count(int unread_count) {
        this.unread_count=unread_count;
    }

    public List<AllMessages> getMessages() {
        return messages;
    }

    public void setMessages(List<AllMessages> messages) {
        this.messages=messages;
    }
}
