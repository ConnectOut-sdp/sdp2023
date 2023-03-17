package com.sdpteam.connectout.chat;

import androidx.lifecycle.LiveData;

import java.util.List;


public interface ChatDirectory {
    /**
     * Get the n last ChatMessages from the chat with the given id
     */
    LiveData<List<ChatMessage>> getMessages(int n, String chatId);

    /**
     * Save your new ChatMessage
     */
    void saveMessage(ChatMessage message);

    String getProfileUserName();
}
