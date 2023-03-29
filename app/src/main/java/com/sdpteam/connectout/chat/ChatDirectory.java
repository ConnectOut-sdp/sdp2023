package com.sdpteam.connectout.chat;

public interface ChatDirectory {

    /**
     * Save your new ChatMessage
     */
    void saveMessage(ChatMessage message);

    String getProfileUserName();
}
