package com.sdpteam.connectout.chat;

public interface ChatDataSource {

    /**
     * Save your new ChatMessage
     */
    void saveMessage(ChatMessage message);

    String getProfileUserName();
}
