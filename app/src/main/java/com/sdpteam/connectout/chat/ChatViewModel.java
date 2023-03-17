package com.sdpteam.connectout.chat;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class ChatViewModel extends ViewModel {
    public ChatModel chatModel;

    public ChatViewModel(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    /**
     * Get the n last ChatMessages from the chat with the given id
     */
    public LiveData<List<ChatMessage>> getMessages(int n, String chatId) {
        return chatModel.getMessages(n, chatId);
    }

    /**
     * Save your new ChatMessage
     */
    public void saveMessage(ChatMessage message) {
        chatModel.saveMessage(message);
    }

    public String getProfileUserName(){
        return chatModel.getProfileUserName();
    }
}




