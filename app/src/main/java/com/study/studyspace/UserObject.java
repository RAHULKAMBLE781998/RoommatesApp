package com.study.studyspace;

import java.io.Serializable;

public class UserObject implements Serializable {
    private String user2id;
    private String user2name;
    private String chatroom =null;
    public void setUser2id(String user2id) {
        this.user2id = user2id;
    }

    public void setUser2name(String user2name) {
        this.user2name = user2name;
    }

    public String getUser2id() {
        return user2id;
    }

    public String getUser2name() {
        return user2name;
    }

    public String getChatroom() {
        return chatroom;
    }

    public void setChatroom(String chatroom) {
        this.chatroom = chatroom;
    }
}
